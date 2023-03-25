package com.cojayero.dogedex3

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex3.api.ApiResponseStatus
import kotlinx.coroutines.launch

private val TAG = DogListViewModel::class.java.simpleName

class DogListViewModel : ViewModel() {
    private val _dogList = MutableLiveData<List<Dog>>()
    val dogList: LiveData<List<Dog>>
        get() = _dogList

    // Estamos poniedo el status como "Any" para que nos sirva en cualquier
    //    tipo de dato
    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>>
        get() = _status


    private val dogRepository = DogRepository()

    init {
        getDogCollection()
    }

    private fun downloadDogs() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleDownloadStatus(dogRepository.downloadDogs())
        }

    }

    private fun downloadUserDogs() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleDownloadStatus(dogRepository.getDogCollection())
        }
    }

    private fun getDogCollection() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleDownloadStatus(dogRepository.getDogCollection())
        }
    }


    @SuppressLint("NullSafeMutableLiveData")
    @Suppress("UNCHECKED_CAST")
    private fun handleDownloadStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dogList.value = apiResponseStatus.data
        }
        _status.value = apiResponseStatus as ApiResponseStatus<Any>
    }

    fun addDogToUser(dogId: Long) {
        // Este no se usa ya, al mver del DogList al dogDetail el añadir el perro

        viewModelScope.launch {
            Log.d(TAG, "DogListViewModel-> $dogId")
            _status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }
    }

    @SuppressLint("NullSafeMutableLiveData")
    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            getDogCollection()
        }
        _status.value = apiResponseStatus
    }

}