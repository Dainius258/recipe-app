package com.kvk.recipeapp.topBarFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R

class RecipeTopFragment(private val recipeTitle: String) : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.fragment_recipe_top, container, false)
        val recipeTitleView = rootView.findViewById<TextView>(R.id.tvRecipeName)
        recipeTitleView.text = recipeTitle
        return rootView
    }
}