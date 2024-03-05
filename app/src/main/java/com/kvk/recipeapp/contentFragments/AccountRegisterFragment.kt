package com.kvk.recipeapp.contentFragments

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import com.kvk.recipeapp.R
class AccountRegisterFragment : Fragment(R.layout.fragment_account_register) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val switchLoginButton: Button = view.findViewById(R.id.btnSwitchLogin)

        switchLoginButton.setOnClickListener{
            val fragmentTransaction = parentFragmentManager.beginTransaction()
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.replace(R.id.flFragmentContents, AccountLoginFragment())
            fragmentTransaction.commit()
        }
    }
}