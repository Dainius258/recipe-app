package com.kvk.recipeapp.fragmentSwitches

import android.app.AlertDialog
import android.content.Context
import androidx.fragment.app.FragmentManager
import com.kvk.recipeapp.R
import com.kvk.recipeapp.bottomBarFragments.FavouriteRecipeAccountBottomFragment
import com.kvk.recipeapp.bottomBarFragments.HomeRecipeAccountBottomFragment
import com.kvk.recipeapp.contentFragments.AccountLoggedInFragment
import com.kvk.recipeapp.contentFragments.AccountLoginFragment
import com.kvk.recipeapp.contentFragments.AddRecipeFragment
import com.kvk.recipeapp.contentFragments.FavouriteRecipeListFragment
import com.kvk.recipeapp.contentFragments.MainRecipeListFragment
import com.kvk.recipeapp.contentFragments.RecipeFragment
import com.kvk.recipeapp.topBarFragments.AddRecipeTopFragment
import com.kvk.recipeapp.topBarFragments.FilterSearchTopFragment
import com.kvk.recipeapp.topBarFragments.LoggedInTopFragment
import com.kvk.recipeapp.topBarFragments.LoginRegisterTopFragment
import com.kvk.recipeapp.topBarFragments.RecipeTopFragment
import com.kvk.recipeapp.utils.TokenManager

class FragmentSwitcher {
    fun switchToRecipe(fragmentManager: FragmentManager, recipeId:Int) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.replace(R.id.flFragmentBottomBar, HomeRecipeAccountBottomFragment())
        fragmentTransaction.replace(R.id.flFragmentContents, RecipeFragment(recipeId))
        fragmentTransaction.replace(R.id.flFragmentTopBar, RecipeTopFragment())
        fragmentTransaction.commit()
    }
    fun switchToHome(fragmentManager: FragmentManager) {
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.addToBackStack(null)

        fragmentTransaction.replace(R.id.flFragmentBottomBar, FavouriteRecipeAccountBottomFragment())
        fragmentTransaction.replace(R.id.flFragmentContents, MainRecipeListFragment())
        fragmentTransaction.replace(R.id.flFragmentTopBar, FilterSearchTopFragment())
        fragmentTransaction.commit()
    }

    fun switchToAddRecipes(fragmentManager: FragmentManager, context: Context) {
        val tokenManager = TokenManager(context)
        val token = tokenManager.getToken()
        if(token != null && tokenManager.isTokenValid(token)) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.flFragmentContents, AddRecipeFragment())
            fragmentTransaction.replace(R.id.flFragmentBottomBar, HomeRecipeAccountBottomFragment())
            fragmentTransaction.replace(R.id.flFragmentTopBar, AddRecipeTopFragment())
            fragmentTransaction.commit()
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder
                .setMessage("Please log in")
                .setTitle("Can't access as a guest")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.replace(R.id.flFragmentContents, AccountLoginFragment())
            fragmentTransaction.replace(R.id.flFragmentBottomBar, HomeRecipeAccountBottomFragment())
            fragmentTransaction.replace(R.id.flFragmentTopBar, LoginRegisterTopFragment())
            fragmentTransaction.commit()
        }
    }

    fun switchToAccount(fragmentManager: FragmentManager, context: Context) {
        val tokenManager = TokenManager(context)
        val token = tokenManager.getToken()
        if(token != null && tokenManager.isTokenValid(token)) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.flFragmentContents, AccountLoggedInFragment())
            fragmentTransaction.replace(R.id.flFragmentTopBar, LoggedInTopFragment())
            fragmentTransaction.commit()
        } else {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.replace(R.id.flFragmentContents, AccountLoginFragment())
            fragmentTransaction.replace(R.id.flFragmentBottomBar, HomeRecipeAccountBottomFragment())
            fragmentTransaction.replace(R.id.flFragmentTopBar, LoginRegisterTopFragment())
            fragmentTransaction.commit()
        }
    }

    fun switchToFavourites(fragmentManager: FragmentManager, context: Context) {
        val tokenManager = TokenManager(context)
        val token = tokenManager.getToken()
        if(token != null && tokenManager.isTokenValid(token)) {
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.replace(R.id.flFragmentBottomBar, HomeRecipeAccountBottomFragment())
            fragmentTransaction.replace(R.id.flFragmentContents, FavouriteRecipeListFragment())
            fragmentTransaction.replace(R.id.flFragmentTopBar, FilterSearchTopFragment())

            fragmentTransaction.commit()
        } else {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder
                .setMessage("Please log in")
                .setTitle("Can't access as a guest")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)

            fragmentTransaction.replace(R.id.flFragmentContents, AccountLoginFragment())
            fragmentTransaction.replace(R.id.flFragmentBottomBar, HomeRecipeAccountBottomFragment())
            fragmentTransaction.replace(R.id.flFragmentTopBar, LoginRegisterTopFragment())
            fragmentTransaction.commit()
        }
    }
}