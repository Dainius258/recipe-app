package com.kvk.recipeapp

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.data.Recipe
import com.kvk.recipeapp.databinding.ItemRecipeBinding

class RecipeAdapter(var recipes: List<Recipe>) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    inner class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(layoutInflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.binding.apply {
            tvRecipeTitle.text = recipes[position].recipe_name
            val base64ImageData = recipes[position].image
            if(base64ImageData != null) {
                val imageData = Base64.decode(base64ImageData, Base64.DEFAULT)
                //Log.d("RECIPE_LIST", imageData.toString())
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0,imageData.size)
                imgRecipe.setImageBitmap(bitmap)
            } else {
                imgRecipe.setImageDrawable(null)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}