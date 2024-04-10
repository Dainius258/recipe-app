package com.kvk.recipeapp

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.data.Recipe
import com.kvk.recipeapp.databinding.ItemRecipeBinding
import com.kvk.recipeapp.utils.TokenManager

class RecipeAdapter(var recipes: List<Recipe>, context: Context) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    inner class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {}
    private val tokenManager = TokenManager(context)
    val token = tokenManager.getToken()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(layoutInflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.binding.apply {
            var checkboxCard = cardviewIsFavourite
            var checkbox = checkboxIsFavourite
            var recipeId = recipes[position].id
            tvRecipeTitle.text = recipes[position].title
            val base64ImageData = recipes[position].image
            if(base64ImageData != null) {
                val imageData = Base64.decode(base64ImageData, Base64.DEFAULT)
                //Log.d("RECIPE_LIST", imageData.toString())
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0,imageData.size)
                imgRecipe.setImageBitmap(bitmap)
                if(token != null && tokenManager.isTokenValid(token)) {
                   checkboxCard.visibility = View.VISIBLE
                } else {
                    checkboxCard.visibility = View.GONE
                }
                checkbox.setOnCheckedChangeListener{ _, isChecked ->
                    Log.d("CHECKBOX", "Recipe ID($recipeId) checked status: $isChecked")
                }
            } else {
                imgRecipe.setImageDrawable(null)
            }
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}