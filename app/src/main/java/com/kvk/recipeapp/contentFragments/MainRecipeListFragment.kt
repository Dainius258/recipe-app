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
            Recipe("Spaghetti Carbonara", false),
            Recipe("Chicken Alfredo", false),
            Recipe("Beef Stroganoff", false),
            Recipe("Shrimp Scampi", false),
            Recipe("Chicken Parmesan", false),
            Recipe("Beef Wellington", false),
            Recipe("Mushroom Risotto", false),
            Recipe("Pad Thai", false),
            Recipe("Sushi Rolls", false),
            Recipe("Lamb Tagine", false),
            Recipe("Eggplant Parmesan", false),
            Recipe("Chicken Curry", false),
            Recipe("Tacos", false),
            Recipe("Salmon Teriyaki", false),
            Recipe("Beef Tacos", false),
            Recipe("Vegetable Stir-Fry", false),
            Recipe("Chicken Fajitas", false),
            Recipe("Pasta Primavera", false),
            Recipe("Vegetable Lasagna", false),
            Recipe("Spinach Salad", false)
        )

        val rootView = inflater.inflate(R.layout.fragment_main_recipe_list, container, false)
        val recyclerView: RecyclerView = rootView.findViewById(R.id.rvRecipes)

        val layoutManager = GridLayoutManager(context, 1)
        recyclerView.layoutManager = layoutManager

        val adapter = RecipeAdapter(recipeList)
        recyclerView.adapter = adapter

        return rootView
    }

}