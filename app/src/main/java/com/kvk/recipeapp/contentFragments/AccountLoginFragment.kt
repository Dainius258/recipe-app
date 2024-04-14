package com.kvk.recipeapp.contentFragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.kvk.recipeapp.R
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

class AccountLoginFragment : Fragment(R.layout.fragment_account_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val switchRegisterButton: Button = view.findViewById(R.id.btnSwitchRegister)
        val loginButton: Button = view.findViewById(R.id.btnLogin)
        val tokenManager = TokenManager(requireContext())
        val fragmentSwitcher = FragmentSwitcher()

        var textInput: TextInputEditText
        var username: String
        var password: String

        switchRegisterButton.setOnClickListener {
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.flFragmentContents, AccountRegisterFragment())
            fragmentTransaction.commit()
        }

        loginButton.setOnClickListener {
            textInput = view.findViewById(R.id.tietLoginUsername)
            username = textInput.text.toString()
            textInput = view.findViewById(R.id.tietPasswordLogin)
            password = textInput.text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                val response = try {
                    RetroFitInstance.api.loginUser(username, password)
                } catch (e: IOException) {
                    Log.e("Network", "IOException: ${e.message}")
                    return@launch
                } catch (e: HttpException) {
                    Log.e("Network", "HttpException: ${e.message}")
                    return@launch
                }
                if (response.isSuccessful && response.body() != null) {
                    withContext(Dispatchers.Main) {
                        val token = response.body()?.token
                        val message = response.body()?.message
                        if (token != null) {
                            tokenManager.saveToken(token)
                        }
                        requireActivity().runOnUiThread {
                            fragmentSwitcher.switchToAccount(parentFragmentManager, requireContext())
                            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
                            builder
                                .setMessage("You have successfully logged in")
                                .setTitle("Success")
                            val dialog: AlertDialog = builder.create()
                            dialog.show()
                        }
                        Log.d("Network", "Response successful $message")
                    }
                } else {
                    val errorMessage = ErrorResponseParser.parseErrorBody(response)
                    if ((errorMessage != null)) {
                        requireActivity().runOnUiThread {
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