package com.cojayero.dogedex3.api

import com.cojayero.dogedex3.*
import com.cojayero.dogedex3.api.dto.AddDogToUserDTO
import com.cojayero.dogedex3.api.dto.SignInDTO
import com.cojayero.dogedex3.api.dto.SignUpDTO
import com.cojayero.dogedex3.api.dto.UserDTO
import com.cojayero.dogedex3.api.responses.AuthApiResponse
import com.cojayero.dogedex3.api.responses.DefaultResponse
import com.cojayero.dogedex3.api.responses.DogListApiResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val logging = HttpLoggingInterceptor()
    .setLevel(HttpLoggingInterceptor.Level.BODY)
private val okHttpClient = OkHttpClient
    .Builder()
    .addInterceptor(ApiServiceInterceptor)
    .addInterceptor(
        logging
    ).build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .baseUrl(BASE_URL)
    .addConverterFactory(MoshiConverterFactory.create())
    .build()

interface ApiService {
    @GET(GET_ALL_DOGS)
    suspend fun getAllDogs(): DogListApiResponse

    @POST(SIGN_UP_URL)
    suspend fun signUp(
        @Body
        signUpDTO: SignUpDTO
    ): AuthApiResponse

   // @Headers("${ApiServiceInterceptor.NEEDS_AUTH_KEY}: true" )
    @POST(SIGN_IN_URL)
    suspend fun login(@Body signInDTO: SignInDTO): AuthApiResponse
    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_KEY}: true" )
    @POST(ADD_DOG_TO_USER)
    suspend fun addDogToUser(@Body addDogToUserDTO: AddDogToUserDTO):DefaultResponse
    @Headers("${ApiServiceInterceptor.NEEDS_AUTH_KEY}: true" )
    @GET(GET_USER_DOGS_URL)
    suspend fun getUserDogs():DogListApiResponse

}

object DogsApi {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

