package com.kvk.recipeapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity


class FavouritesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourites)

        val homeButton: ImageButton = findViewById<ImageButton>(R.id.imageButtonHome);

        homeButton.setOnClickListener {
           val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}