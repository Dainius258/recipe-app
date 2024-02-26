package com.kvk.recipeapp.bottomBarFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import com.kvk.recipeapp.R
import com.kvk.recipeapp.contentFragments.FavouriteRecipeListFragment

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
            fragmentTransaction.replace(R.id.flFragmentContents, FavouriteRecipeListFragment())
            fragmentTransaction.commit()
        }
    }
}