package com.cojayero.dogedex3.dogdetail

import android.annotation.SuppressLint
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex3.Dog
import com.cojayero.dogedex3.DogRepository
import com.cojayero.dogedex3.api.ApiResponseStatus
import kotlinx.coroutines.launch

private val TAG = DogDetailActivity::class.java.simpleName
class DogDetailViewModel:ViewModel()
{
    private val _dogList = MutableLiveData<List<Dog>>()
    val dogList: LiveData<List<Dog>>
        get() = _dogList

    // Estamos poniedo el status como "Any" para que nos sirva en cualquier
    //    tipo de dato
    private val _status = MutableLiveData<ApiResponseStatus<Any>>()
    val status: LiveData<ApiResponseStatus<Any>>
        get() = _status


    private val dogRepository = DogRepository()

    fun addDogToUser(dogId: Long) {
        viewModelScope.launch {
            Log.d(TAG, "DogListViewModel-> $dogId")
            _status.value = ApiResponseStatus.Loading()
            handleAddDogToUserResponseStatus(dogRepository.addDogToUser(dogId))
        }
    }
    @SuppressLint("NullSafeMutableLiveData")
    private fun handleAddDogToUserResponseStatus(apiResponseStatus: ApiResponseStatus<Any>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {

        }
        _status.value = apiResponseStatus
    }
}