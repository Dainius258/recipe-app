package com.kvk.recipeapp.contentFragments

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatEditText
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.adapters.CommentAdapter
import com.kvk.recipeapp.adapters.IngredientAdapter
import com.kvk.recipeapp.R
import com.kvk.recipeapp.utils.RetroFitInstance
import com.kvk.recipeapp.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RecipeFragment(private val recipeId: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val tokenManager = TokenManager(requireContext())
        val token = tokenManager.getToken()

        val rootView = inflater.inflate(R.layout.fragment_recipe, container, false)

        val recyclerViewIngredients: RecyclerView = rootView.findViewById(R.id.rvIngredients)
        val recyclerViewComments: RecyclerView = rootView.findViewById(R.id.rvComments)

        val layoutManagerIngredients = GridLayoutManager(context, 1)
        recyclerViewIngredients.layoutManager = layoutManagerIngredients

        val layoutManagerComments = GridLayoutManager(context, 1)
        recyclerViewComments.layoutManager = layoutManagerComments

        val scrollView = rootView.findViewById<ScrollView>(R.id.scrollViewRecipe)
        val loadingBar = rootView.findViewById<ProgressBar>(R.id.loadingProgressBar)

        val recipeImage = rootView.findViewById<ImageView>(R.id.imgvRecipe)
        val ratingText = rootView.findViewById<TextView>(R.id.tvRating)
        val cookTime = rootView.findViewById<TextView>(R.id.tvTimeToCook)
        val servings = rootView.findViewById<TextView>(R.id.tvServings)
        val guide = rootView.findViewById<TextView>(R.id.tvGuide)
        val iMadeThisButton = rootView.findViewById<Button>(R.id.btnRateRecipe)
        val makeCommentButton = rootView.findViewById<Button>(R.id.btnMakeComment)

        if(token != null && tokenManager.isTokenValid(token)) {
            iMadeThisButton.visibility = View.VISIBLE
            makeCommentButton.visibility = View.VISIBLE
        } else {
            iMadeThisButton.visibility = View.GONE
            makeCommentButton.visibility = View.GONE
        }

        GlobalScope.launch(Dispatchers.IO)  {
            val response = try {
                RetroFitInstance.api.getRecipeById(recipeId)
            } catch (e: IOException) {
                Log.e("Network", "IOException: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Network", "HttpException: ${e.message}")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val recipe = response.body()!!

                    val base64ImageData = recipe.image
                    val imageData = Base64.decode(base64ImageData, Base64.DEFAULT)
                    val bitmap = BitmapFactory.decodeByteArray(imageData, 0,imageData.size)
                    recipeImage.setImageBitmap(bitmap)

                    val ingredients = recipe.ingredients
                    val ingredientAdapter = IngredientAdapter(ingredients)

                    val totalTimeMinutes = recipe.total_time_minutes
                    val cookHours = totalTimeMinutes / 60
                    val cookMinutes = totalTimeMinutes % 60

                    ratingText.text = "${recipe.rating}% Liked this recipe"

                    if(cookHours < 1) {
                        cookTime.text = "Ready in ${cookMinutes} minutes"
                    } else {
                        cookTime.text = "Ready in ${cookHours} hours ${cookMinutes} minutes"
                    }

                    if(recipe.servings == 1) {
                        servings.text = "${recipe.servings} serving"
                    } else {
                        servings.text = "${recipe.servings} servings"
                    }

                    guide.text = recipe.guide
                    recyclerViewIngredients.adapter = ingredientAdapter

                    makeCommentButton.setOnClickListener {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_write_comment, null)
                        builder.setView(dialogLayout)
                        val dialog: AlertDialog = builder.create()
                        dialogLayout.findViewById<Button>(R.id.btnCancelComment).setOnClickListener {
                            dialog.dismiss()
                        }
                        dialogLayout.findViewById<Button>(R.id.btnSubmitComment).setOnClickListener {
                            val comment = dialogLayout.findViewById<AppCompatEditText>(R.id.etmlComment).text.toString()
                            Log.d("COMMENT", comment)
                            val userId = tokenManager.getUserId()?.toInt()
                            if (userId != null) {
                                postComment(recipeId, userId, comment)
                                dialog.dismiss()
                                showCommentPostedDialog()
                                updateCommentList(recyclerViewComments)
                            } else {
                                dialog.dismiss()
                            }
                        }
                        dialog.show()
                    }
                    // Very bad idea but im out of time
                    updateCommentList(recyclerViewComments)
                    scrollView.visibility = View.VISIBLE
                    loadingBar.visibility = View.GONE
                }
            } else {
                Log.e("Network", "Response not successful")
            }
        }
        return rootView
    }

    private fun postComment(recipeId: Int, userId: Int, comment: String, ) {
        GlobalScope.launch(Dispatchers.IO)  {
            val response = try {
                RetroFitInstance.api.postComment(recipeId, userId, comment)
            } catch (e: IOException) {
                Log.e("Network", "IOException: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Network", "HttpException: ${e.message}")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    //val message = response.body()!!
                    // TODO Add a check if the comment is too short
                }
            } else {
                Log.d("Network", "Response not successful")
            }
        }
    }

    private fun updateCommentList(recyclerViewComments: RecyclerView) {
        GlobalScope.launch(Dispatchers.IO)  {
            val response = try {
                RetroFitInstance.api.getCommentsByRecipeId(recipeId)
            } catch (e: IOException) {
                Log.e("Network", "IOException: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Network", "HttpException: ${e.message}")
                return@launch
            }
            if(response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    val comments = response.body()!!
                    val commentAdapter = CommentAdapter(comments, requireContext())
                    recyclerViewComments.adapter = commentAdapter
                }
            } else {
                Log.d("Network", "No comments")
            }
        }
    }

    private fun showCommentPostedDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Comment Posted")
        builder.setMessage("Your comment has been successfully posted.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }
}