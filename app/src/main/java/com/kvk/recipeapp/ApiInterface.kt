package com.kvk.recipeapp

import com.kvk.recipeapp.data.Recipes
import retrofit2.Response
import retrofit2.http.GET

interface ApiInterface {
    @GET("/recipes/getrecipes")
    suspend fun getAllRecipes():Response<Recipes>
}