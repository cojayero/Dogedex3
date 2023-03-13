package com.cojayero.dogedex3.auth.ui.main

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.databinding.FragmentSignUpBinding
import com.cojayero.dogedex3.isValidEmail

private val TAG = SignUpFragment::class.java.simpleName
class SignUpFragment : Fragment() {
    interface SignUpFragmentActions {
        fun onSignUpFieldsValidated(email: String, password: String, passwordConfirmation: String)
    }

    private lateinit var signUpFragmentActions: SignUpFragmentActions
    override fun onAttach(context: Context) {
        super.onAttach(context)
        signUpFragmentActions = try {
            context as SignUpFragmentActions
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement LoginFragmentActions")
        }
    }

    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater)
        setupSignupButton()
        return binding.root
    }

    private fun setupSignupButton() {
        binding.signUpButton.setOnClickListener {
            validateFields()
        }
    }

    private fun validateFields() {
        binding.emailInput.error = ""
        binding.passwordEdit.error = ""
        binding.confirmPasswordInput.error = ""
        val email = binding.emailEdit.text.toString()
        if (!isValidEmail(email)) {
            binding.emailInput.error = getString(R.string.email_is_not_valid)
            return
        }

        val password = binding.passwordEdit.text.toString()
        if (password.isEmpty()) {
            binding.passwordEdit.error = getString(R.string.password_is_not_valid)
            return
        }
        val passwordConfimation = binding.confirmPasswordEdit.text.toString()
        if (passwordConfimation.isEmpty()) {
            binding.confirmPasswordInput.error =
                getString(R.string.confirmation_password_cant_be_empty)
            return
        }
        if (password != passwordConfimation) {
            binding.passwordEdit.error = getString(R.string.passwords_doesnt_match)
            return
        }
        signUpFragmentActions.onSignUpFieldsValidated(email, password, passwordConfimation)
    }


}