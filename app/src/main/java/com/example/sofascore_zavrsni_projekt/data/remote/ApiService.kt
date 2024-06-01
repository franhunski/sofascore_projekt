package com.example.sofascore_zavrsni_projekt.data.remote

import retrofit2.http.GET

interface ApiService {

    @GET("sports")
    suspend fun getSports(

    )
}