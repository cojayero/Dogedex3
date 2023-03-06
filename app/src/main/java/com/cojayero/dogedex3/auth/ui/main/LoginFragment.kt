package com.cojayero.dogedex3.auth.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

}