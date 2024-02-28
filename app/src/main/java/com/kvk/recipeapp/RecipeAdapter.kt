package com.kvk.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.data.Recipe

class RecipeAdapter(
    var recipes: List<Recipe>
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
        inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}