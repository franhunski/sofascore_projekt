package com.example.sofascore_zavrsni_projekt.data.remote

import okhttp3.Interceptor
import okhttp3.Response
class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request)
    }
}