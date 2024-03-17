package com.kvk.recipeapp.bottomBarFragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R
import com.kvk.recipeapp.fragmentSwitches.FragmentSwitcher

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 */
class FavouriteRecipeAccountBottomFragment : Fragment(R.layout.fragment_favourite_recipe_account_bottom) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val favouriteButton: ImageButton = view.findViewById(R.id.imageButtonFavourites)
        val accountButton: ImageButton = view.findViewById(R.id.imageButtonAccount)
        val addRecipeButton: ImageButton = view.findViewById(R.id.imageButtonAddRecipe)
        val fragmentSwitcher = FragmentSwitcher()

        addRecipeButton.setOnClickListener{
            fragmentSwitcher.switchToAddRecipes(parentFragmentManager, requireContext())
        }

        favouriteButton.setOnClickListener{
            fragmentSwitcher.switchToFavourites(parentFragmentManager, requireContext())
        }

        accountButton.setOnClickListener{
            fragmentSwitcher.switchToAccount(parentFragmentManager, requireContext())
        }
    }
}