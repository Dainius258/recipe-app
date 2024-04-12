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
import com.kvk.recipeapp.RecipeAdapter
import com.kvk.recipeapp.utils.RetroFitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class MainRecipeListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_main_recipe_list, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rvRecipes)

        val layoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager
        val loadingBar = rootView.findViewById<ProgressBar>(R.id.loadingProgressBar)


        GlobalScope.launch(Dispatchers.IO)  {
            val response = try {
                RetroFitInstance.api.getAllRecipes()
            } catch (e: IOException) {
                Log.e("Network", "IOException: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Network", "HttpException: ${e.message}")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    //Log.d("ResponseBody", "THEBODYOFRECIPE: ${response.body()}")
                    val recipeList = response.body()!!
                    val adapter = RecipeAdapter(recipeList, requireContext())
                    adapter.setFragmentManager(parentFragmentManager)
                    recyclerView.adapter = adapter

                    recyclerView.visibility = View.VISIBLE
                    loadingBar.visibility= View.GONE
                }
            } else {
                Log.e("Network", "Response not successful")
            }
        }

        return rootView
    }

}