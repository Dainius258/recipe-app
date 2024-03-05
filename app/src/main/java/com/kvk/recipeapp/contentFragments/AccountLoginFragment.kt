package com.kvk.recipeapp.contentFragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R

class AccountLoginFragment : Fragment(R.layout.fragment_account_login) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val switchRegisterButton: Button = view.findViewById(R.id.btnSwitchRegister)

        switchRegisterButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.flFragmentContents, AccountRegisterFragment())
            fragmentTransaction.commit()
        }
    }
}