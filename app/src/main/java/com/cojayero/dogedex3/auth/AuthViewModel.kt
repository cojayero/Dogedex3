package com.cojayero.dogedex3.auth

import android.annotation.SuppressLint
import android.provider.Settings.Global.getString
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex3.api.ApiResponseStatus
import kotlinx.coroutines.launch
import java.security.AccessController.getContext

private val TAG= AuthViewModel::class.java.simpleName
class AuthViewModel : ViewModel() {
    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    private val _status = MutableLiveData<ApiResponseStatus<User>>()
    val status: LiveData<ApiResponseStatus<User>>
        get() = _status
    private val authRepository = AuthRepository()

    fun signUp(email: String, password: String, passwordConfirmation: String) {
        Log.d(TAG,"email $email, password $password confirm $passwordConfirmation")
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(authRepository.signUp(email, password, passwordConfirmation))
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun handleResponseStatus(apiResponseStatus: ApiResponseStatus<User>) {
        Log.d(TAG, "........ Handle response status")
        if (apiResponseStatus is ApiResponseStatus.Success) {
            Log.d(TAG,"Response status is success ${apiResponseStatus.data}")
            _user.value = apiResponseStatus.data
        }
        _status.value = apiResponseStatus
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleResponseStatus(authRepository.login(email, password))
        }
    }

}