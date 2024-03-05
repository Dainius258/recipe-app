package com.kvk.recipeapp.bottomBarFragments

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R
import com.kvk.recipeapp.contentFragments.AccountLoginFragment
import com.kvk.recipeapp.contentFragments.MainRecipeListFragment
import com.kvk.recipeapp.topBarFragments.FilterSearchTopFragment
import com.kvk.recipeapp.topBarFragments.LoginRegisterTopFragment

class HomeRecipeAccountBottomFragment : Fragment(R.layout.fragment_home_recipe_account_bottom) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeButton: ImageButton = view.findViewById(R.id.imageButtonHome)
        val  accountButton: ImageButton = view.findViewById(R.id.imageButtonAccount)

        homeButton.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.replace(R.id.flFragmentBottomBar, FavouriteRecipeAccountBottomFragment())
            fragmentTransaction.replace(R.id.flFragmentContents, MainRecipeListFragment())
            fragmentTransaction.replace(R.id.flFragmentTopBar, FilterSearchTopFragment())

            fragmentTransaction.commit()
        }

        accountButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.replace(R.id.flFragmentContents, AccountLoginFragment())
            fragmentTransaction.replace(R.id.flFragmentTopBar, LoginRegisterTopFragment())
            fragmentTransaction.replace(R.id.flFragmentBottomBar, HomeRecipeAccountBottomFragment())
            fragmentTransaction.commit()
        }
    }
}