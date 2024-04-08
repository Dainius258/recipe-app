package com.kvk.recipeapp.utils

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE)

    fun saveSelectedTagIds(tagIds: Set<String>) {
        val editor = sharedPreferences.edit()
        editor.putStringSet("selectedTagIds", tagIds)
        editor.apply()
    }

    fun getSelectedTagIds(): Set<String>? {
        return sharedPreferences.getStringSet("selectedTagIds", null)
    }
}