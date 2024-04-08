package com.kvk.recipeapp

import com.kvk.recipeapp.data.GeneralResponse
import com.kvk.recipeapp.data.LoginResponse
import com.kvk.recipeapp.data.Recipes
import com.kvk.recipeapp.data.Tags
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiInterface {
    @GET("/api/getrecipes")
    suspend fun getAllRecipes():Response<Recipes>
    @GET("/api/gettags")
    suspend fun getAllTags():Response<Tags>
    @POST("/api/register")
    @FormUrlEncoded
    suspend fun registerUser(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("email") email: String
    ): Response<GeneralResponse>
    @POST("/api/login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String,
    ): Response<LoginResponse>
    @POST("/api/newrecipe")
    @FormUrlEncoded
    suspend fun postRecipe(
        @Field("user_id") user_id: Int,
        @Field("title") title: String,
        @Field("image") image: String,
        @Field("ingredients") ingredients: Array<String>,
        @Field("guide") guide: String,
        @Field("total_time_minutes") total_time_minutes: Int,
        @Field("servings") servings: Int,
        @Field("tag_ids") tag_ids: Set<String>?
    ): Response<GeneralResponse>
}