package com.cojayero.dogedex3.dogdetail

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex3.DogRepository
import com.cojayero.dogedex3.api.ApiResponseStatus
import kotlinx.coroutines.launch

private val TAG = DogDetailComposeActivity::class.java.simpleName

class DogDetailComposeViewModel : ViewModel() {
    var status = mutableStateOf<ApiResponseStatus<Any>?>(null)
        private set
    private val dogRepository = DogRepository()

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            Log.d(TAG, "DogListViewModel-> $dogId")
            status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
        }
        status.value = apiResponseStatus
    }

    fun resetAPIResponseStatus() {
        status.value = null
    }
}