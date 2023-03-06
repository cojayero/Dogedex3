package com.cojayero.dogedex3

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cojayero.dogedex3.api.ApiResponseStatus
import kotlinx.coroutines.launch

class DogListViewModel : ViewModel() {
    private val _dogList = MutableLiveData<List<Dog>>()
    val dogList: LiveData<List<Dog>>
        get() = _dogList

    private val _status = MutableLiveData<ApiResponseStatus<List<Dog>>>()
    val status: LiveData<ApiResponseStatus<List<Dog>>>
        get() = _status


    private val dogRepository = DogRepository()

    init {
        downloadDogs()
    }

    private fun downloadDogs() {
        viewModelScope.launch {
            _status.value = ApiResponseStatus.Loading()
            handleDownloadStatus(dogRepository.downloadDogs())
        }

    }

    private fun handleDownloadStatus(apiResponseStatus: ApiResponseStatus<List<Dog>>) {
        if (apiResponseStatus is ApiResponseStatus.Success) {
            _dogList.value = apiResponseStatus.data
        }
        _status.value = apiResponseStatus
    }
}