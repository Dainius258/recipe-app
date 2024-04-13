package com.kvk.recipeapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.kvk.recipeapp.R
import com.kvk.recipeapp.bottomBarFragments.FavouriteRecipeAccountBottomFragment
import com.kvk.recipeapp.contentFragments.MainRecipeListFragment
import com.kvk.recipeapp.topBarFragments.FilterSearchTopFragment

val TAG = "TAG"
class MainActivity : AppCompatActivity(), FilterSearchTopFragment.OnSearchQuerySubmitListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val filterSearchFragment = FilterSearchTopFragment();
        filterSearchFragment.setSearchQuerySubmitListener(this)
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentTopBar, filterSearchFragment)
            commit()
        }

        val favRecAccFragment = FavouriteRecipeAccountBottomFragment();
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentBottomBar, favRecAccFragment)
            commit()
        }

        val recipeListFragment = MainRecipeListFragment();
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContents, recipeListFragment)
            commit()
        }
    }

    override fun onSearchQuerySubmit(query: String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.flFragmentContents)
        if (fragment is MainRecipeListFragment) {
            fragment.onSearchQuerySubmit(query)
        }
    }

    override fun onSearchQueryNewText(query: String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.flFragmentContents)
        if (fragment is MainRecipeListFragment) {
            fragment.onSearchQueryNewText(query)
        }
    }
}