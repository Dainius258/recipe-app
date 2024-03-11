package com.kvk.recipeapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kvk.recipeapp.R
import com.kvk.recipeapp.bottomBarFragments.FavouriteRecipeAccountBottomFragment
import com.kvk.recipeapp.contentFragments.MainRecipeListFragment
import com.kvk.recipeapp.topBarFragments.FilterSearchTopFragment

val TAG = "TAG"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val FilterSearchFragment = FilterSearchTopFragment();
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentTopBar, FilterSearchFragment)
            commit()
        }

        val FavRecAccFragment = FavouriteRecipeAccountBottomFragment();
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentBottomBar, FavRecAccFragment)
            commit()
        }

        val RecipeListFragment = MainRecipeListFragment();
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentContents, RecipeListFragment)
            commit()
        }
    }
}