package com.kvk.recipeapp.bottomBarFragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R
import com.kvk.recipeapp.fragmentSwitches.FragmentSwitcher

class HomeRecipeAccountBottomFragment : Fragment(R.layout.fragment_home_recipe_account_bottom) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeButton: ImageButton = view.findViewById(R.id.imageButtonHome)
        val accountButton: ImageButton = view.findViewById(R.id.imageButtonAccount)
        val addRecipeButton: ImageButton = view.findViewById(R.id.imageButtonAddRecipe)
        val fragmentSwitcher = FragmentSwitcher();

        addRecipeButton.setOnClickListener{
            fragmentSwitcher.switchToAddRecipes(parentFragmentManager, requireContext())
        }

        homeButton.setOnClickListener {
            fragmentSwitcher.switchToHome(parentFragmentManager)
        }

        accountButton.setOnClickListener{
           fragmentSwitcher.switchToAccount(parentFragmentManager, requireContext())
        }
    }
}