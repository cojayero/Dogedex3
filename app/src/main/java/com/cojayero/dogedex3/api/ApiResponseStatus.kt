package com.cojayero.dogedex3.api

import com.cojayero.dogedex3.Dog
// Generalizamos el  API, cambiando el tipo por un tipo genérico.
sealed class ApiResponseStatus<T>() {
    class Success<T>(val data:T):ApiResponseStatus<T>()
    class Loading<T>():ApiResponseStatus<T>()
    class Error<T>(val messageId:Int):ApiResponseStatus<T>()
}