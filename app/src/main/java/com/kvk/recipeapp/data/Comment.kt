package com.kvk.recipeapp.data

import java.sql.Timestamp

data class Comment(
    val id: Int,
    val recipe_id: Int,
    val user_id: Int,
    var comment_text: String,
    val created_at: Timestamp,
    val updated_at: Timestamp,
    val username: String
)
