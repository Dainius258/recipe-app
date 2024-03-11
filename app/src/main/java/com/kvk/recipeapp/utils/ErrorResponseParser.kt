package com.kvk.recipeapp.utils

import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

object ErrorResponseParser {
    fun parseErrorBody(response: Response<*>): String? {
        val errorMessage = response.errorBody()?.string()
        return if (!errorMessage.isNullOrEmpty()) {
            try {
                val errorJson = JSONObject(errorMessage)
                errorJson.getString("message")
            } catch (e: JSONException) {
                null
            }
        } else {
            null
        }
    }
}