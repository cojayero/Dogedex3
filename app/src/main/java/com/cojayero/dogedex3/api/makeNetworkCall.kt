package com.cojayero.dogedex3.api

import com.cojayero.dogedex3.R
import com.cojayero.dogedex3.api.dto.DogDTOMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.UnknownHostException

suspend fun <T> makeNetworkCall(
    call: suspend () -> T
):ApiResponseStatus<T>{
    return withContext(Dispatchers.IO) {
        try {
            ApiResponseStatus.Success(call())
        } catch (e: UnknownHostException) {
            ApiResponseStatus.Error(R.string.unknown_host_exception)
        } catch (e: Exception) {
            ApiResponseStatus.Error(R.string.unknown_error)
        }

    }
}