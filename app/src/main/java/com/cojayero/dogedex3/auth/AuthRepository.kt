package com.cojayero.dogedex3.auth

import android.util.Log
import com.cojayero.dogedex3.api.ApiResponseStatus
import com.cojayero.dogedex3.api.DogsApi
import com.cojayero.dogedex3.api.dto.SignInDTO
import com.cojayero.dogedex3.api.dto.SignUpDTO
import com.cojayero.dogedex3.api.dto.UserDTOMapper
import com.cojayero.dogedex3.api.makeNetworkCall

private val TAG = AuthRepository::class.java.simpleName

class AuthRepository {
    suspend fun signUp(
        email: String,
        password: String,
        passwordConfirmation: String
    ): ApiResponseStatus<User> = makeNetworkCall {
        val sigUpDTO = SignUpDTO(email, password, passwordConfirmation)
        val signUpResponse = DogsApi.retrofitService.signUp(sigUpDTO)
        if (!signUpResponse.isSuccess) {
            throw Exception(signUpResponse.message)
        }
        val userDTO = signUpResponse.data.user
        val userDTOMapper = UserDTOMapper()
        Log.d(TAG, "_________----____$userDTO")
        userDTOMapper.fromUserDTOtoUserDomain(userDTO)
    }

    suspend fun login(email: String, password: String): ApiResponseStatus<User> = makeNetworkCall {

        val loginDTO = SignInDTO(email, password)
        val loginResponse = DogsApi.retrofitService.login(loginDTO)
        if (!loginResponse.isSuccess) {
            throw Exception(loginResponse.message)
        }

        val userDTO = loginResponse.data.user
        val userDTOMapper = UserDTOMapper()
        Log.d(TAG, "$userDTO")
        userDTOMapper.fromUserDTOtoUserDomain(userDTO)

    }
}