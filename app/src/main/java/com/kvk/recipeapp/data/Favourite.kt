package com.kvk.recipeapp.data

import java.sql.Timestamp

data class Favourite(
    val id: Int,
    val user_id: Int,
    val recipe_id: Int,
    val created_at: Timestamp,
    val updated_at: Timestamp
)
