package com.kvk.recipeapp.contentFragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import com.kvk.recipeapp.R
import com.kvk.recipeapp.adapters.IngredientAddAdapter
import com.kvk.recipeapp.adapters.TagAdapter
import com.kvk.recipeapp.callbacks.CropResultCallback
import com.kvk.recipeapp.utils.ErrorResponseParser
import com.kvk.recipeapp.utils.ImageUtils
import com.kvk.recipeapp.utils.PreferenceManager
import com.kvk.recipeapp.utils.RetroFitInstance
import com.kvk.recipeapp.utils.TokenManager
import com.yalantis.ucrop.UCrop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.File
import java.io.IOException


class AddRecipeFragment : Fragment() {

    private val preferenceManager: PreferenceManager by lazy {
        PreferenceManager(requireContext())
    }

    private var cropResultCallback: CropResultCallback? = null
    private lateinit var croppedImageUri: String

    private lateinit var ingredientAddAdapter: IngredientAddAdapter
    private lateinit var ingredientsList: MutableList<String>

    companion object {
        private const val CAMERA = Manifest.permission.CAMERA
        private const val READ_IMAGES = Manifest.permission.READ_EXTERNAL_STORAGE
        private const val WRITE_IMAGES = Manifest.permission.WRITE_EXTERNAL_STORAGE
    }

    private val permissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

        }

    private fun isPermissionGranted(name: String) = ContextCompat.checkSelfPermission(
        requireContext(), name
    ) == PackageManager.PERMISSION_GRANTED

    private fun permissionCheck() {
        val camera = isPermissionGranted(CAMERA)
        val readimages = isPermissionGranted(READ_IMAGES)
        val writeimages =  isPermissionGranted(WRITE_IMAGES)

        when {
            !readimages or !camera or !writeimages -> {
                //Log.d("PERMISSIONS", "camera NOT GRANTED")
                permissionRequest.launch(arrayOf(READ_IMAGES, CAMERA, WRITE_IMAGES))
            }
        }

    }
    private fun startUCrop(sourceUri: Uri) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, "cropped_image"))
        val options = UCrop.Options().apply {
            setCompressionQuality(70)
            withAspectRatio(19F,9F)
            // Other options here
        }
        val uCrop = UCrop.of(sourceUri, destinationUri)
            .withOptions(options)

        // Start uCrop activity using the cropImage contract
        cropImage.launch(uCrop.getIntent(requireContext()))
    }

    private val cropImage = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUtils = ImageUtils()
            val resultUri = result.data?.let { UCrop.getOutput(it) }
            // Handle the cropped image URI here
            if (resultUri != null) {
                val resultBase64 = imageUtils.uriToBase64(requireContext(),resultUri)
                cropResultCallback?.onCropSuccess(resultBase64)
                //Log.d("UCrop", "Cropped URI: $resultBase64")
            } else {
                cropResultCallback?.onCropFailure(NullPointerException("Cropped URI is null"))
               // Log.d("UCrop", "Cropping failed")
            }
        } else if (result.resultCode == UCrop.RESULT_ERROR) {
            val cropError = UCrop.getError(result.data!!)
            cropResultCallback?.onCropFailure(cropError ?: Exception("Unknown cropping error"))
            //Log.e("UCrop", "Error cropping image", cropError)
        }
    }
    private fun setCropResultCallback(callback: CropResultCallback) {
        cropResultCallback = callback
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        preferenceManager.clearSelectedTagIds()
        val view = inflater.inflate(R.layout.fragment_add_recipe, container, false)

        val tokenManager = TokenManager(requireContext())

        val btnPlusIngredient = view.findViewById<ImageButton>(R.id.btnPlusIngredient)
        val btnPlusImage = view.findViewById<ImageButton>(R.id.btnPlusImage)
        val btnPostRecipe = view.findViewById<Button>(R.id.btnPostRecipe)
        val tietIngredientInput = view.findViewById<TextInputEditText>(R.id.tietIngredientInput)
        val rvIngredients = view.findViewById<RecyclerView>(R.id.rvIngredients)
        val btnPlusTag = view.findViewById<ImageButton>(R.id.btnPlusTag)

        permissionCheck() //

        val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                //Log.d("PhotoPicker", "Selected URI: $uri")
                startUCrop(uri)
            } else {
                //Log.d("PhotoPicker", "No media selected")
            }
        }

        setCropResultCallback(object : CropResultCallback {
            override fun onCropSuccess(uri: String?) {
                Log.d("UCrop", "Cropped URI: $uri")
                if (uri != null) {
                    croppedImageUri = uri
                }
            }

            override fun onCropFailure(error: Throwable) {
                Log.e("UCrop", "Error cropping image", error)
            }
        })

        ingredientsList = mutableListOf()
        ingredientAddAdapter = IngredientAddAdapter(ingredientsList)

        rvIngredients.adapter = ingredientAddAdapter
        rvIngredients.layoutManager = LinearLayoutManager(requireContext())

        btnPlusIngredient.setOnClickListener {
            val ingredient = tietIngredientInput.text.toString().trim()
            if (ingredient.isNotEmpty()) {
                ingredientsList.add(ingredient)
                ingredientAddAdapter.notifyDataSetChanged()
                tietIngredientInput.text?.clear()
            }
        }

        btnPlusImage.setOnClickListener{
            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        btnPlusTag.setOnClickListener{

            GlobalScope.launch(Dispatchers.IO)  {
                val response = try {
                    RetroFitInstance.api.getAllTags()
                } catch (e: IOException) {
                    Log.e("Network", "IOException: ${e.message}")
                    return@launch
                } catch (e: HttpException) {
                    Log.e("Network", "HttpException: ${e.message}")
                    return@launch
                }
                if(response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        val tagList = response.body()!!
                        val dialogLayout = LayoutInflater.from(context).inflate(R.layout.dialog_tags_layout, null)
                        val recyclerView = dialogLayout.findViewById<RecyclerView>(R.id.dialogRecyclerView)
                        recyclerView.layoutManager = GridLayoutManager(context, 3)
                        val adapter = TagAdapter(tagList, preferenceManager)
                        recyclerView.adapter = adapter
                        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                        builder
                            .setView(dialogLayout)
                            .setTitle("Select tags")
                            .setPositiveButton("Close") { dialog, _ ->
                                dialog.dismiss()
                            }
                        val dialog: AlertDialog = builder.create()
                        dialog.show()
                    }
                } else {
                    Log.e("Network", "Response not successful")
                }
            }
        }

        btnPostRecipe.setOnClickListener {
           val recipeTitle = view.findViewById<TextInputEditText>(R.id.tietRecipeTitleInput).text.toString()
            val recipeIngredients = ingredientsList.toTypedArray()
            val recipeGuide = view.findViewById<EditText>(R.id.tietRecipeGuide).text.toString()
            var recipeMinutesText = view.findViewById<TextInputEditText>(R.id.tietMinutesInput).text.toString()
            var recipeHoursText = view.findViewById<TextInputEditText>(R.id.tietHoursInput).text.toString()
            val recipeServingsText = view.findViewById<TextInputEditText>(R.id.tietServingsInput).text.toString()
            val recipeTags = preferenceManager.getSelectedTagIds()

            if (recipeTitle.isEmpty() || recipeGuide.isEmpty() ||
                (recipeMinutesText.isEmpty() && recipeHoursText.isEmpty()) ||
                recipeServingsText.isEmpty()
            ) {
                Log.d("POST_RECIPE", "FILL ALL THE FIELDS")
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder
                    .setMessage("Fill all the fields")
                    .setTitle("Missing fields")
                    .setPositiveButton("Close") { dialog, _ ->
                        dialog.dismiss()
                    }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                return@setOnClickListener // Exits
            }

            if(recipeIngredients.size <= 1) {
                Log.d("POST_RECIPE", "ADD ATLEAST TWO INGREDIENTS")
                val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
                builder
                    .setMessage("Add at least two ingredients")
                    .setTitle("Missing ingredients")
                    .setPositiveButton("Close") { dialog, _ ->
                        dialog.dismiss()
                    }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                return@setOnClickListener
            }

            if (recipeTags != null) {
                if(recipeTags.size <= 1) {
                    Log.d("POST_RECIPE", "ADD ATLEAST TWO TAGS")
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder
                        .setMessage("Add at least two tags")
                        .setTitle("Missing tags")
                        .setPositiveButton("Close") { dialog, _ ->
                            dialog.dismiss()
                        }
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                    return@setOnClickListener
                }
            }

            if(!::croppedImageUri.isInitialized) {
                Log.d("POST_RECIPE", "Cropped image uri is not initialized")
                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                builder
                    .setMessage("Select an image of your dish")
                    .setTitle("No image selected")
                    .setPositiveButton("Close") { dialog, _ ->
                        dialog.dismiss()
                    }
                val dialog: AlertDialog = builder.create()
                dialog.show()
                return@setOnClickListener
            }
            val recipeImage = croppedImageUri

            var recipeMinutes: Int
            val recipeHours: Int
            val recipeServings: Int

            if(recipeMinutesText.isEmpty()) {
                recipeMinutesText = "0"
            }
            if(recipeHoursText.isEmpty()) {
                recipeHoursText = "0"
            }

            try {
                recipeMinutes = recipeMinutesText.toInt()
                recipeHours = recipeHoursText.toInt() * 60
                recipeMinutes += recipeHours
                recipeServings = recipeServingsText.toInt()
            } catch (e: NumberFormatException) {
                Log.d("POST_RECIPE", "Wrong numeric input")
                return@setOnClickListener // Exits
            }
            Log.d("RESULT_CHECK", "TITLE: $recipeTitle  INGREDIENTS: ${recipeIngredients.contentToString()} GUIDE: $recipeGuide" +
                    " MINUTES: $recipeMinutes SERVINGS: $recipeServings")
            val userId = tokenManager.getUserId()?.toInt()

            GlobalScope.launch(Dispatchers.IO) {
                val response = try {
                    // TODO add real user id from the token
                    if (userId != null) {
                        RetroFitInstance.api.postRecipe(userId, recipeTitle, recipeImage,recipeIngredients,recipeGuide,recipeMinutes,recipeServings, recipeTags)
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
                if(response.isSuccessful) {
                    withContext(Dispatchers.Main) {
                        requireActivity().runOnUiThread{
                            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                            builder
                                .setMessage("Recipe successfully added")
                                .setTitle("It sounds tasty!")
                                .setPositiveButton("Close") { dialog, _ ->
                                    dialog.dismiss()
                                }
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                        preferenceManager.clearSelectedTagIds()
                        Log.d("Network", "Response successful")
                    }
                } else {
                    val errorMessage = ErrorResponseParser.parseErrorBody(response)
                    if((errorMessage != null)) {
                        requireActivity().runOnUiThread{
                            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                            builder
                                .setMessage("Please try again")
                                .setTitle(errorMessage)
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                        Log.d("Network", "Response unsuccessful $errorMessage")
                    } else {
                        Log.e("Network", "Response unsuccessful HTTP Status Code: ${response.code()}")
                    }
                }
            }
        }
        return view
    }
}