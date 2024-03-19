package com.kvk.recipeapp.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException
import java.io.IOException


class ImageUtils {
    fun uriToBase64(context: Context, uri: Uri?): String? {
        try {
            val inputStream = context.contentResolver.openInputStream(uri!!)

            val bitmap = BitmapFactory.decodeStream(inputStream)

            inputStream!!.close()

            return bitmapToBase64(bitmap)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun bitmapToBase64(bitmap: Bitmap?): String? {
        val byteArrayOutputStream = ByteArrayOutputStream()
        // Compress the bitmap to a ByteArrayOutputStream
        bitmap?.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        // Convert ByteArrayOutputStream to byte array
        val byteArray = byteArrayOutputStream.toByteArray()
        // Encode the byte array to Base64 string
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}