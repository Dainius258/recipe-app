package com.kvk.recipeapp.callbacks

interface CropResultCallback {
    fun onCropSuccess(uri: String?)
    fun onCropFailure(error: Throwable)
}