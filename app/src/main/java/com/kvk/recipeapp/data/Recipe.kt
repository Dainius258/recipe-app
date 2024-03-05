package com.kvk.recipeapp.data
import java.sql.Timestamp
public data class Recipe(
    val recipe_id: Int,
    val recipe_name: String,
    val user_id: Int,
    val image: String,
    val ingredients: Array<String>,
    val total_time: String,
    val servings: Int,
    val created_at: Timestamp,
    val updated_at: Timestamp
)
