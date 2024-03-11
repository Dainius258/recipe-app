package com.kvk.recipeapp.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.auth0.android.jwt.JWT
import java.util.Date

class TokenManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    companion object {
        @Volatile
        private var instance: TokenManager? = null

        fun getInstance(context: Context): TokenManager {
            return instance ?: synchronized(this) {
                instance ?: TokenManager(context.applicationContext).also { instance = it }
            }
        }
    }

    fun saveToken(token: String) {
        editor.putString("jwtToken", token)
        editor.apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("jwtToken", null)
    }

    fun clearToken() {
        editor.remove("jwtToken")
        editor.apply()
    }
    fun isTokenValid(token: String): Boolean {
        try {
            val jwt = JWT(token)
            val expirationDate = jwt.expiresAt
            val currentDate = Date()

            return expirationDate != null && currentDate.before(expirationDate)
        } catch (e: Exception) {
            Log.e("TokenValidation", "Error validating token", e)
            return false
        }
    }
}