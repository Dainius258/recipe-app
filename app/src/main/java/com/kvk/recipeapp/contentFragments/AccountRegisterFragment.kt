package com.kvk.recipeapp.contentFragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.kvk.recipeapp.R
import com.kvk.recipeapp.utils.ErrorResponseParser
import com.kvk.recipeapp.utils.PreferenceManager
import com.kvk.recipeapp.utils.RetroFitInstance
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class AccountRegisterFragment : Fragment(R.layout.fragment_account_register) {
    private val preferenceManager: PreferenceManager by lazy {
        PreferenceManager(requireContext())
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        preferenceManager.clearSelectedTagIds()
        super.onViewCreated(view, savedInstanceState)
        val switchLoginButton: Button = view.findViewById(R.id.btnSwitchLogin)
        val registerButton: Button = view.findViewById(R.id.btnRegister)
        var textInput: TextInputEditText
        var username: String
        var email: String
        var password: String
        var passwordRepeat: String

        switchLoginButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.flFragmentContents, AccountLoginFragment())
            fragmentTransaction.commit()
        }

        registerButton.setOnClickListener {
            textInput = view.findViewById(R.id.tietRegisterUsername)
            username = textInput.text.toString()
            textInput = view.findViewById(R.id.tietRegisterEmail)
            email = textInput.text.toString()
            textInput = view.findViewById(R.id.tietRegisterPassword)
            password = textInput.text.toString()
            textInput = view.findViewById(R.id.tietRegisterRepeatPassword)
            passwordRepeat = textInput.text.toString()

            if(password != passwordRepeat) {
                requireActivity().runOnUiThread{
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                    builder
                        .setMessage("Please try again")
                        .setTitle("*Passwords don't match")
                    val dialog: AlertDialog = builder.create()
                    dialog.show()
                }
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    val response = try {
                        RetroFitInstance.api.registerUser(username, password, email)
                    } catch (e: IOException) {
                        Log.e("Network", "IOException: ${e.message}")
                        return@launch
                    } catch (e: HttpException) {
                        Log.e("Network", "HttpException: ${e.message}")
                        return@launch
                    }
                    if(response.isSuccessful && response.body() != null) {
                        withContext(Dispatchers.Main) {
                            val message = response.body()?.message
                            requireActivity().runOnUiThread{
                                val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                                builder
                                    .setMessage("Please log in")
                                    .setTitle(message)
                                    .setPositiveButton("Login") { dialog, id ->
                                        switchLoginButton.performClick()
                                    }
                                    .setNegativeButton("Later") { dialog, id ->
                                        dialog.cancel()
                                    }
                                val dialog: AlertDialog = builder.create()
                                dialog.show()
                            }
                            Log.d("Network", "Response successful $message")
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
                            Log.e("Network", "Response unsuccessful")
                        }
                    }
                }
            }
        }
    }
}