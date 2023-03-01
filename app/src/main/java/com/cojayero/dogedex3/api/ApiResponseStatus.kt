package com.cojayero.dogedex3.api

import com.cojayero.dogedex3.Dog

sealed class ApiResponseStatus {
    class Success(val dogList:List<Dog>):ApiResponseStatus()
    class Loading():ApiResponseStatus()
    class Error(val messageId:Int):ApiResponseStatus()
}