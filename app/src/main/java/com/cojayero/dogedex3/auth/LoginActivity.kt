package com.cojayero.dogedex3.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.cojayero.dogedex3.main.MainActivity
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.api.ApiResponseStatus
import com.cojayero.dogedex3.auth.ui.main.LoginFragment
import com.cojayero.dogedex3.auth.ui.main.LoginFragmentDirections
import com.cojayero.dogedex3.auth.ui.main.SignUpFragment
import com.cojayero.dogedex3.databinding.ActivityLoginBinding

private val TAG = LoginActivity::class.java.simpleName

class LoginActivity() : AppCompatActivity(), LoginFragment.LoginFragmentActions,
    SignUpFragment.SignUpFragmentActions {

    private val viewModel by viewModels<AuthViewModel>()
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.status.observe(this) { status ->
            Log.d(TAG, "Observe - status $status")
            when (status) {
                is ApiResponseStatus.Error -> {
                    hideLoading(true)
                    showErrorDialog(status.messageId)
                }
                is ApiResponseStatus.Loading -> hideLoading(false)
                is ApiResponseStatus.Success -> hideLoading(true)
            }
        }
        viewModel.user.observe(this) { user ->
            Log.d(TAG, "Observe user -------> $user")
            if (user != null) {
                User.setLoggedInUser(this,user)
                startMainActivity()
            }
        }
    }

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun showErrorDialog(messageId: Int) {
        val mensaje = getString(messageId)

        Log.d(TAG, "$mensaje")
        AlertDialog.Builder(this).setTitle(R.string.there_was_an_error)
            .setMessage(messageId)
            .setPositiveButton(android.R.string.ok) { _, _ -> /* Dismiss dialog */
            }.create()
    }

    override fun onRegisterButtonClick() {
        findNavController(R.id.nav_host_fragment).navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
    }

    override fun onSignInFieldsValidate(email: String, password: String) {
        viewModel.login(email, password)
    }


    override fun onSignUpFieldsValidated(
        email: String,
        password: String,
        passwordConfirmation: String
    ) {
        viewModel.signUp(email, password, passwordConfirmation)
    }

    private fun hideLoading(hide: Boolean) {
        Log.d(TAG, "hideLoading $hide")
        if (hide) {
            binding.loadingWheel.visibility = View.GONE
        } else {
            binding.loadingWheel.visibility = View.GONE
        }
    }
}