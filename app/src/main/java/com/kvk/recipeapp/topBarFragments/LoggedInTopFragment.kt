package com.kvk.recipeapp.topBarFragments

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R
import com.kvk.recipeapp.utils.TokenManager

class LoggedInTopFragment : Fragment(R.layout.fragment_loggedin_top) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tokenManager = TokenManager(requireContext())
        val userTitle: TextView = view.findViewById(R.id.tvUser)
        userTitle.text = "Welcome ${tokenManager.getUsername()}"
    }
}