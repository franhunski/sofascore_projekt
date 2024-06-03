package com.example.sofascore_zavrsni_projekt.data.remote

import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.SportsResponse
import retrofit2.http.GET

interface ApiService {

    @GET("sports")
    suspend fun getSports(

    ) : SportsResponse
}