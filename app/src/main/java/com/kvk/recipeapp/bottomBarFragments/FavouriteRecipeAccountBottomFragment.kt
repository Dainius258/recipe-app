package com.kvk.recipeapp.bottomBarFragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R
import com.kvk.recipeapp.contentFragments.AccountLoginFragment
import com.kvk.recipeapp.contentFragments.FavouriteRecipeListFragment
import com.kvk.recipeapp.topBarFragments.FilterSearchTopFragment

// TODO: Rename parameter arguments, choose names that match

/**
 * A simple [Fragment] subclass.
 */
class FavouriteRecipeAccountBottomFragment : Fragment(R.layout.fragment_favourite_recipe_account_bottom) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val favouriteButton: ImageButton = view.findViewById(R.id.imageButtonFavourites)
        val  accountButton: ImageButton = view.findViewById(R.id.imageButtonAccount)

        favouriteButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.replace(R.id.flFragmentBottomBar, HomeRecipeAccountBottomFragment())
            fragmentTransaction.replace(R.id.flFragmentContents, FavouriteRecipeListFragment())
            fragmentTransaction.replace(R.id.flFragmentTopBar, FilterSearchTopFragment())

            fragmentTransaction.commit()
        }

        accountButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.replace(R.id.flFragmentContents, AccountLoginFragment())
            fragmentTransaction.replace(R.id.flFragmentBottomBar, HomeRecipeAccountBottomFragment())
            fragmentTransaction.commit()
        }
    }
}