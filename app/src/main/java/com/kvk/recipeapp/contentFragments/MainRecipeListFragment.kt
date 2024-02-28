package com.kvk.recipeapp.contentFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.R
import com.kvk.recipeapp.RecipeAdapter
import com.kvk.recipeapp.data.Recipe

class MainRecipeListFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var recipeList = mutableListOf(
            Recipe("Delicious boorgir", false),
            Recipe("Delicious boorgir", false),
            Recipe("Delicious boorgir", false),
            Recipe("Delicious boorgir", false),
            Recipe("Delicious boorgir", false),
            Recipe("Delicious boorgir", false),
            Recipe("Delicious boorgir", false),
        )

        val rootView = inflater.inflate(R.layout.fragment_main_recipe_list, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rvRecipes)

        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager

        val adapter = RecipeAdapter(recipeList)
        recyclerView.adapter = adapter

        return rootView
    }

}