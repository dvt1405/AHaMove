package com.kt.apps.media.core.network

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor() : Interceptor {
    private var _token: String = TEMP_TOKEN
    override fun intercept(chain: Interceptor.Chain): Response {
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "token $_token")
            .build()
        return chain.proceed(newRequest)
    }

    fun setToken(token: String) {
        _token = token
    }

    companion object {
        private const val TEMP_TOKEN =
            "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"
    }
}
