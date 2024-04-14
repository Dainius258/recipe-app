package com.kvk.recipeapp.data
import java.sql.Timestamp
data class Recipe(
    val id: Int,
    val user_id: Int,
    val title: String,
    val image: String,
    val ingredients: Array<String>,
    val guide: String,
    val total_time_minutes: Int,
    val servings: Int,
    val rating: Float,
    val created_at: Timestamp,
    val updated_at: Timestamp,
    val likes: Int,
    val dislikes: Int
)
