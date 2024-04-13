package com.kvk.recipeapp.adapters

import android.content.Context
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.kvk.recipeapp.data.Recipe
import com.kvk.recipeapp.databinding.ItemRecipeBinding
import com.kvk.recipeapp.fragmentSwitches.FragmentSwitcher
import com.kvk.recipeapp.utils.ErrorResponseParser
import com.kvk.recipeapp.utils.RetroFitInstance
import com.kvk.recipeapp.utils.TokenManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class RecipeAdapter(private var recipes: List<Recipe>, context: Context) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {
    inner class RecipeViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {}
    private val tokenManager = TokenManager(context)
    private val token = tokenManager.getToken()
    private var favoriteRecipeIds: HashSet<Int> = HashSet()
    private lateinit var fragmentManager: FragmentManager

    init {
        if (token != null && tokenManager.isTokenValid(token)) {
            val userId = tokenManager.getUserId()?.toInt()
            if (userId != null) {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        val response = RetroFitInstance.api.getFavourites(userId)
                        if (response.isSuccessful && response.body() != null) {
                            val favoriteList = response.body()!!
                            favoriteRecipeIds.addAll(favoriteList.map { it.recipe_id })
                            Log.d("RECIPEIDS", "$favoriteRecipeIds")
                            withContext(Dispatchers.Main) {
                                notifyDataSetChanged() // Notify adapter once data is fetched
                            }
                        } else {
                            Log.e("Network", "Failed to fetch favorite recipe IDs")
                        }
                    } catch (e: IOException) {
                        Log.e("Network", "IOException: ${e.message}")
                    } catch (e: HttpException) {
                        Log.e("Network", "HttpException: ${e.message}")
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecipeBinding.inflate(layoutInflater, parent, false)
        return RecipeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.binding.apply {
            var checkboxCard = cardviewIsFavourite
            //var checkbox = checkboxIsFavourite
            var recipeId = recipes[position].id
            val recipeTitle = recipes[position].title
            val isFavorite = favoriteRecipeIds.contains(recipeId)
            tvRecipeTitle.text = recipes[position].title
            val base64ImageData = recipes[position].image
            val imageButton = imgRecipe

            if(base64ImageData != null) {
                val imageData = Base64.decode(base64ImageData, Base64.DEFAULT)
                //Log.d("RECIPE_LIST", imageData.toString())
                val bitmap = BitmapFactory.decodeByteArray(imageData, 0,imageData.size)
                imgRecipe.setImageBitmap(bitmap)
                if(token != null && tokenManager.isTokenValid(token)) {
                   checkboxCard.visibility = View.VISIBLE
                } else {
                    checkboxCard.visibility = View.GONE
                }

                imageButton.setOnClickListener {
                    val fragmentSwitcher = FragmentSwitcher()
                    fragmentSwitcher.switchToRecipe(fragmentManager, recipeId, recipeTitle)
                }

                checkboxIsFavourite.isChecked = isFavorite
                checkboxIsFavourite.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) {
                        addFavorite(recipeId)
                    } else {
                        removeFavorite(recipeId)
                    }
                }
            } else {
                imgRecipe.setImageDrawable(null)
            }
        }
    }
    override fun getItemCount(): Int {
        return recipes.size
    }

    private fun addFavorite(recipeId: Int) {
        // Add recipe ID to favorites
        var userId = tokenManager.getUserId()?.toInt()
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                if (userId != null) {
                    RetroFitInstance.api.postNewFavourite(userId, recipeId)
                } else {
                    return@launch
                }
            } catch (e: IOException) {
                Log.e("Network", "IOException: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Network", "HttpException: ${e.message}")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    Log.d("Network", "Response successful")
                }
            } else {
                val errorMessage = ErrorResponseParser.parseErrorBody(response)
                if ((errorMessage != null)) {

                    Log.d("Network", "Response unsuccessful $errorMessage")
                } else {
                    Log.e("Network", "Response unsuccessful")
                }
            }
        }
    }

    private fun removeFavorite(recipeId: Int) {
        var userId = tokenManager.getUserId()?.toInt()
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                if (userId != null) {
                    RetroFitInstance.api.deleteFavourite(userId, recipeId)
                } else {
                    return@launch
                }
            } catch (e: IOException) {
                Log.e("Network", "IOException: ${e.message}")
                return@launch
            } catch (e: HttpException) {
                Log.e("Network", "HttpException: ${e.message}")
                return@launch
            }
            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    Log.d("Network", "Response successful")
                }
            } else {
                val errorMessage = ErrorResponseParser.parseErrorBody(response)
                if ((errorMessage != null)) {

                    Log.d("Network", "Response unsuccessful $errorMessage")
                } else {
                    Log.e("Network", "Response unsuccessful")
                }
            }
        }

    }

    fun setFragmentManager(manager: FragmentManager) {
        fragmentManager = manager
    }
}