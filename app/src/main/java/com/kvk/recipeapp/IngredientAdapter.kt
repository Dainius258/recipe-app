package com.kvk.recipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IngredientAdapter(private val ingredients: MutableList<String>) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_ingredient, parent, false)
        return IngredientViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val ingredient = ingredients[position]
        holder.bind(ingredient)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    inner class IngredientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val btnMinusIngredient: ImageButton = itemView.findViewById(R.id.btnMinusIngredient)

        fun bind(ingredient: String) {
            textView.text = ingredient
            btnMinusIngredient.setOnClickListener {
                // Implement the logic to remove the item when the minus button is clicked
                ingredients.removeAt(bindingAdapterPosition)
                notifyDataSetChanged()
            }
        }
    }
}