package com.kvk.recipeapp

import com.kvk.recipeapp.data.Recipe
import com.kvk.recipeapp.data.RecipeRating
import com.kvk.recipeapp.data.UpdatedComment
import com.kvk.recipeapp.data.lists.Comments
import com.kvk.recipeapp.data.lists.Favourites
import com.kvk.recipeapp.data.lists.Recipes
import com.kvk.recipeapp.data.lists.Tags
import com.kvk.recipeapp.data.responses.GeneralResponse
import com.kvk.recipeapp.data.responses.LoginResponse
import com.kvk.recipeapp.data.responses.RecipeRatingResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @GET("/api/getrecipes")
    suspend fun getAllRecipes():Response<Recipes>
    @GET("/api/getuserrecipes/{user_id}")
    suspend fun getUserRecipes(@Path("user_id") id: Int):Response<Recipes>
    @GET("/api/recipe/{id}")
    suspend fun getRecipeById(@Path("id") id: Int): Response<Recipe>
    @GET("/api/comments/{id}")
    suspend fun getCommentsByRecipeId(@Path("id") id: Int): Response<Comments>
    @PUT("/api/updatecomment/{id}")
    suspend fun updateComment(
        @Path("id") id: Int,
        @Body requestBody: UpdatedComment
    ): Response<GeneralResponse>
    @PUT("/api/rate/{id}")
    suspend fun rateRecipe(
        @Path("id") id: Int,
        @Body requestBody: RecipeRating
    ): Response<RecipeRatingResponse>
    @DELETE("/api/deletecomment/{id}")
    suspend fun deleteCommentById(@Path("id") id: Int): Response<GeneralResponse>
    @POST("/api/getfavouriterecipes") // TODO: Needs to be replaced with GET ASAP
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
    @GET("/recipesbyids")
    fun getRecipesByIds(@Query("ids") ids: List<Int>): Call<List<Recipe>>
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

    @POST("/api/newcomment")
    @FormUrlEncoded
    suspend fun postComment(
        @Field("recipe_id") recipe_id: Int,
        @Field("user_id") user_id: Int,
        @Field("comment_text") comment_text: String
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