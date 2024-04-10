package com.kvk.recipeapp.contentFragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R
import com.kvk.recipeapp.fragmentSwitches.FragmentSwitcher
import com.kvk.recipeapp.utils.TokenManager

class AccountLoggedInFragment : Fragment(R.layout.fragment_account_logged_in) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val logoutButton: Button = view.findViewById(R.id.btnLogout)
        val debugButton: Button = view.findViewById(R.id.btnDebug)
        val fragmentSwitcher = FragmentSwitcher()

        val tokenText: TextView = view.findViewById<TextView>(R.id.tvToken)
        val tokenManager = TokenManager(requireContext())

        tokenText.text = tokenManager.getToken()

        logoutButton.setOnClickListener{
            tokenManager.clearToken()
            fragmentSwitcher.switchToHome(parentFragmentManager)
        }

        debugButton.setOnClickListener {
            val userId = tokenManager.getUserId()
            val builder: AlertDialog.Builder = AlertDialog.Builder(context)
            builder
                .setMessage("${userId} your user id")
                .setTitle("DEBUG")
                .setPositiveButton("Ok") { dialog, _ ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}