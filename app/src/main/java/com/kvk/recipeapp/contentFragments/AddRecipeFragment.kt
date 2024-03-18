package com.kvk.recipeapp.contentFragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.kvk.recipeapp.IngredientAdapter
import com.kvk.recipeapp.R

class AddRecipeFragment : Fragment() {
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var ingredientsList: MutableList<String>
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        val btnPlusIngredient = view.findViewById<ImageButton>(R.id.btnPlusIngredient)
        val tietIngredientInput = view.findViewById<TextInputEditText>(R.id.tietIngredientInput)
        val rvIngredients = view.findViewById<RecyclerView>(R.id.rvIngredients)

        ingredientsList = mutableListOf()
        ingredientAdapter = IngredientAdapter(ingredientsList)

        rvIngredients.adapter = ingredientAdapter
        rvIngredients.layoutManager = LinearLayoutManager(requireContext())

        btnPlusIngredient.setOnClickListener {
            val ingredient = tietIngredientInput.text.toString().trim()
            if (ingredient.isNotEmpty()) {
                ingredientsList.add(ingredient)
                ingredientAdapter.notifyDataSetChanged()
                tietIngredientInput.text?.clear()
            }
        }

        return view
    }
}