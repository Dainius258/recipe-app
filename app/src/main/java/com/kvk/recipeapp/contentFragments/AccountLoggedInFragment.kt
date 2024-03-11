package com.kvk.recipeapp.contentFragments

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
        val fragmentSwitcher = FragmentSwitcher()

        val tokenText: TextView = view.findViewById<TextView>(R.id.tvToken)
        val tokenManager = TokenManager(requireContext())

        tokenText.text = tokenManager.getToken()

        logoutButton.setOnClickListener{
            tokenManager.clearToken()
            fragmentSwitcher.switchToHome(parentFragmentManager)
        }
    }
}