package com.kvk.recipeapp.contentFragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.IngredientAdapter
import com.kvk.recipeapp.R
import com.kvk.recipeapp.utils.RetroFitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RecipeFragment(private val recipeId: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_recipe, container, false)
        val recyclerViewIngredients: RecyclerView = rootView.findViewById(R.id.rvIngredients)

        val layoutManager = GridLayoutManager(context, 1)
        recyclerViewIngredients.layoutManager = layoutManager


        GlobalScope.launch(Dispatchers.IO)  {
            val response = try {
                RetroFitInstance.api.getRecipeById(recipeId)
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
                    val recipe = response.body()!!
                    val ingredients = recipe.ingredients
                    val adapter = IngredientAdapter(ingredients)
                    recyclerViewIngredients.adapter = adapter
                }
            } else {
                Log.e("Network", "Response not successful")
            }
        }

        return rootView
    }
}