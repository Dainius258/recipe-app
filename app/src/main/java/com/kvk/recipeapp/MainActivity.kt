package com.kvk.recipeapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.OnClickAction
import android.util.Log
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageButton
import kotlin.math.log

val TAG = "TAG"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val HomeRecAccFragment = HomeRecipeAccountBottomFragment();
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragmentBottomBar, HomeRecAccFragment)
            commit()
        }
    }
}