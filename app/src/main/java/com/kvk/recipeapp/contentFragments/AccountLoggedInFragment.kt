package com.kvk.recipeapp.contentFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.R
import com.kvk.recipeapp.adapters.RecipeAdapter
import com.kvk.recipeapp.fragmentSwitches.FragmentSwitcher
import com.kvk.recipeapp.utils.PreferenceManager
import com.kvk.recipeapp.utils.RetroFitInstance
import com.kvk.recipeapp.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AccountLoggedInFragment : Fragment() {
    private lateinit var adapter: RecipeAdapter
    private val preferenceManager: PreferenceManager by lazy {
        PreferenceManager(requireContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferenceManager.clearSelectedTagIds()
        val rootView = inflater.inflate(R.layout.fragment_account_logged_in, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rvRecipesUser)
        val fragmentSwitcher = FragmentSwitcher()
        val tokenManager = TokenManager(requireContext())

        val logoutButton: Button = rootView.findViewById(R.id.btnLogout)
        val layoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager

        getUserRecipes(recyclerView, rootView, tokenManager)

        logoutButton.setOnClickListener {
            tokenManager.clearToken()
            fragmentSwitcher.switchToHome(parentFragmentManager)
        }
        return rootView
    }

    fun getUserRecipes(recyclerView: RecyclerView, rootView: View, tokenManager: TokenManager) {
        GlobalScope.launch(Dispatchers.IO)  {
            val response = try {
                RetroFitInstance.api.getUserRecipes(tokenManager.getUserId()!!.toInt())
            } catch (e: IOException) {
                Log.e("Network", "IOException: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Network", "HttpException: ${e.message}")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val recipeList = response.body()!!
                    adapter = RecipeAdapter(recipeList, requireContext())
                    adapter.setFragmentManager(parentFragmentManager)
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
                    rootView.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                }
            } else {
                Log.e("Network", "Response not successful")
                rootView.findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
                rootView.findViewById<TextView>(R.id.tvNoRecipes).visibility = View.VISIBLE
            }
        }
    }
}