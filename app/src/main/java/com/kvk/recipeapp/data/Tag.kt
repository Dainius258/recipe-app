package com.kvk.recipeapp.data

import java.sql.Timestamp

data class Tag(
    val id: Int,
    val tag_name: String,
    val created_at: Timestamp,
    val updated_at: Timestamp
)
