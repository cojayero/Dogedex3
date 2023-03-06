package com.cojayero.dogedex3.auth.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.cojayero.dogedex3.databinding.FragmentSignUpBinding


class SignUpFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val binding = FragmentSignUpBinding.inflate(inflater)
        return binding.root
    }


}