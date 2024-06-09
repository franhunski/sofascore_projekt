package com.example.sofascore_zavrsni_projekt.data.remote

import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Incident
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.time.Duration.Companion.seconds
import kotlin.time.toJavaDuration

const val BASE_URL = "https://academy-backend.sofascore.dev/"

object Network {

    private var INSTANCE: ApiService? = null

    private val okHttpClient = OkHttpClient().newBuilder()
        .addInterceptor(AuthInterceptor())
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(10.seconds.toJavaDuration()).build()

    private val gson = GsonBuilder()
        .registerTypeAdapter(Incident::class.java, IncidentDeserializer())
        .create()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(okHttpClient)
        .build()


    fun getInstance(): ApiService {
        if (INSTANCE == null) {
            INSTANCE = retrofit.create(ApiService::class.java)
        }
        return INSTANCE!!
    }

}