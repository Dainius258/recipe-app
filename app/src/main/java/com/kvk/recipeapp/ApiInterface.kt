package com.kvk.recipeapp

import com.kvk.recipeapp.data.Comments
import com.kvk.recipeapp.data.Favourites
import com.kvk.recipeapp.data.GeneralResponse
import com.kvk.recipeapp.data.LoginResponse
import com.kvk.recipeapp.data.Recipe
import com.kvk.recipeapp.data.Recipes
import com.kvk.recipeapp.data.Tags
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiInterface {
    @GET("/api/getrecipes")
    suspend fun getAllRecipes():Response<Recipes>
    @GET("/api/recipe/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): Response<Recipe>
    @GET("/api/comments/{id}")
    suspend fun getCommentsByRecipeId(@Path("id") id: Int): Response<Comments>
    @POST("/api/getfavouriterecipes")
    @FormUrlEncoded
    suspend fun getFavouriteRecipes(
        @Field("user_id") user_id:Int
    ):Response<Recipes>
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

    @POST("/api/newfavourite")
    @FormUrlEncoded
    suspend fun postNewFavourite(
        @Field("user_id") user_id: Int,
        @Field("recipe_id") recipe_id: Int
    ): Response<GeneralResponse>

    @POST("/api/getfavourites")
    @FormUrlEncoded
    suspend fun getFavourites(
        @Field("user_id") user_id: Int
    ):Response<Favourites>

    @POST("/api/deletefavourite")
    @FormUrlEncoded
    suspend fun deleteFavourite(
        @Field("user_id") user_id: Int,
        @Field("recipe_id") recipe_id: Int
    ):Response<GeneralResponse>



}