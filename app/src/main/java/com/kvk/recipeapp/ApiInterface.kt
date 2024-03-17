package com.kvk.recipeapp

import com.kvk.recipeapp.data.LoginResponse
import com.kvk.recipeapp.data.Recipes
import com.kvk.recipeapp.data.RegisterResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("/api/getrecipes")
    suspend fun getAllRecipes():Response<Recipes>
    @POST("/api/register")
    @FormUrlEncoded
    suspend fun registerUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String
    ): Response<RegisterResponse>
    @POST("/api/login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Response<LoginResponse>
}