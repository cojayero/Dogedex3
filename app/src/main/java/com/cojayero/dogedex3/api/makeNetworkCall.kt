package com.cojayero.dogedex3.api

import android.util.Log
import com.cojayero.dogedex3.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.Exception
import java.net.UnknownHostException

private const val TAG = "makeNetworkCall --- "
private const val UNAUTHORIZED_ERROR_CODE = 401
suspend fun <T> makeNetworkCall(
    call: suspend () -> T
): ApiResponseStatus<T> {
    return withContext(Dispatchers.IO) {
        try {
            Log.d(TAG,"--->  Haciendo la llamada")
            ApiResponseStatus.Success(call())
        } catch (e: UnknownHostException) {
            Log.d(TAG, "Unknown host exception")
            ApiResponseStatus.Error(R.string.unknown_host_exception)
        }
    catch(e:HttpException) {

        val errorMessage = if(e.code() == UNAUTHORIZED_ERROR_CODE) {
            R.string.wrong_user_or_password
        } else {
            R.string.unknown_error
        }
        ApiResponseStatus.Error(errorMessage)
    }
        catch (e: Exception) {
            val errorMessage = when (e.message) {
                "sign_up_error" -> R.string.error_sign_up
                "sign_in_error" -> R.string.error_sign_in
                "user_already_exists" -> R.string.user_already_exists
                "error_adding_dog"-> R.string.error_adding_dog
                else -> R.string.unknown_error
            }

            Log.d(TAG, "Excepcion ->$e.message")
            Log.d(TAG, "Data : $e.data")
            Log.d(TAG, "Error ----->>>>$errorMessage")
            ApiResponseStatus.Error(errorMessage)
        }

    }
}