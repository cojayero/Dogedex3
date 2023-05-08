package com.cojayero.dogedex3

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex3.api.ApiResponseStatus
import kotlinx.coroutines.launch


private val TAG = DogListComposeViewModel::class.java.simpleName

class DogListComposeViewModel : ViewModel() {
    var dogList = mutableStateOf<List<Dog>>(listOf())
        private set
    var status = mutableStateOf<ApiResponseStatus<Any>?>(null)
        private set
    private val dogRepository = DogRepository()
    init {
        getDogCollection()
    }

    private fun downloadDogs() {
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleDownloadStatus(dogRepository.downloadDogs())
        }

    }

    private fun downloadUserDogs() {
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleDownloadStatus(dogRepository.getDogCollection())
        }
    }

    private fun getDogCollection() {
        viewModelScope.launch {
            status.value = ApiResponseStatus.Loading()
            handleDownloadStatus(dogRepository.getDogCollection())
        }
    }


    @SuppressLint("NullSafeMutableLiveData")
    @Suppress("UNCHECKED_CAST")
    private fun handleDownloadStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            dogList.value = apiResponseStatus.data
        }
        status.value = apiResponseStatus as ApiResponseStatus<Any>
    }

    fun addDogToUser(dogId: Long) {
        // Este no se usa ya, al mver del DogList al dogDetail el añadir el perro

        viewModelScope.launch {
            Log.d(TAG, "DogListViewModel-> $dogId")
            status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            getDogCollection()
        }
        status.value = apiResponseStatus
    }

}