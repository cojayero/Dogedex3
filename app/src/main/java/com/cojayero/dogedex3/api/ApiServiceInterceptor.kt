package com.cojayero.dogedex3.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

private const val TAG = "ApiServiceInterceptor"

object ApiServiceInterceptor : Interceptor {
    const val NEEDS_AUTH_KEY = "needs_authentication"
    private var sessionToken: String? = null
    fun setSessionToken(sessionToken: String) {
        Log.d(TAG, "getSessionToken $sessionToken")
        this.sessionToken = sessionToken
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder = request.newBuilder()
        val headers = request.headers(NEEDS_AUTH_KEY)
        Log.d(TAG, "$headers  ${headers.javaClass.simpleName}")
        if (request.headers(NEEDS_AUTH_KEY).isNotEmpty()) {
            // needs credentials
            Log.d(TAG, "Intercept: needs credentials")
            if (sessionToken == null) {
                Log.d(TAG, "intercept:session token is null")
                throw RuntimeException("Need to be authenticated")
            } else {
                Log.d(TAG, "Intercept: Auth-token: $sessionToken")
                requestBuilder.addHeader("AUTH-TOKEN", sessionToken!!)
            }
        } else {
            Log.d(TAG, "header :: ->$request.headers(NEEDS_AUTH_KEY)")
        }
        return chain.proceed(requestBuilder.build())
    }
}