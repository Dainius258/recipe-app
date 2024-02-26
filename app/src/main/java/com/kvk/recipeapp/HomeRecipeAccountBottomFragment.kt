package com.kvk.recipeapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 */
class HomeRecipeAccountBottomFragment : Fragment(R.layout.fragment_home_recipe_account_bottom) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeButton: ImageButton = view.findViewById(R.id.imageButtonHome)

        homeButton.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.flFragmentBottomBar, FavouriteRecipeAccountBottomFragment())
            fragmentTransaction.commit()

        }
    }
}