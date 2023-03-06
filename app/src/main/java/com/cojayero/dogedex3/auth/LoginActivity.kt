package com.cojayero.dogedex3.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.auth.ui.main.LoginFragment
import com.cojayero.dogedex3.auth.ui.main.LoginFragmentDirections

class LoginActivity : AppCompatActivity(), LoginFragment.LoginFragmentActions {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

    }

    override fun onRegisterButtonClick() {
        findNavController(R.id.nav_host_fragment).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }
}