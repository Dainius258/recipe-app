package com.kvk.recipeapp.contentFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.R
import com.kvk.recipeapp.adapters.RecipeAdapter
import com.kvk.recipeapp.topBarFragments.FilterSearchTopFragment
import com.kvk.recipeapp.utils.RetroFitInstance
import com.kvk.recipeapp.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class FavouriteRecipeListFragment : Fragment(), FilterSearchTopFragment.OnSearchQueryListener {
    private lateinit var adapter: RecipeAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_favourite_recipe_list, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rvFavouriteRecipes)

        val layoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager

        val tokenManager = TokenManager(requireContext())
        val userId = tokenManager.getUserId()?.toInt()
        val loadingBar = rootView.findViewById<ProgressBar>(R.id.loadingProgressBar)

        if (userId != null) {
            getFavouriteRecipes(userId, recyclerView, loadingBar)
        }

        val filterFragment = parentFragmentManager.findFragmentById(R.id.flFragmentTopBar)
        if(filterFragment is FilterSearchTopFragment) {
            filterFragment.setSearchQueryListener(this)
        }
        return rootView
    }

    fun getFavouriteRecipes(userId: Int, recyclerView: RecyclerView, loadingBar:ProgressBar) {
        GlobalScope.launch(Dispatchers.IO)  {
            val response = try {
                if (userId != null) {
                    RetroFitInstance.api.getFavouriteRecipes(userId)
                } else {
                    return@launch
                }
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
                    adapter = RecipeAdapter(recipeList,requireContext())
                    adapter.setFragmentManager(parentFragmentManager)
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
                    loadingBar.visibility = View.GONE
                }
            } else {
                Log.e("Network", "Response not successful")
            }
        }
    }

    override fun onSearchQuery(query: String) {
        adapter.filterRecipesByName(query)
    }

    override fun onSearchTextChange(query: String) {
        if(query == "") {
            adapter.resetFilter()
        }
    }
}