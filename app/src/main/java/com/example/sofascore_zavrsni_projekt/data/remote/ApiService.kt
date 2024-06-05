package com.example.sofascore_zavrsni_projekt.data.remote

import android.media.Image
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Event
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Sport
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("sports")
    suspend fun getSports() : List<Sport>

    @GET("sport/{slug}/events/{date}")
    suspend fun getEventsForSportAndDate(
        @Path("slug") slug: String,
        @Path("date") date: String
    ) : List<Event>

    @GET("team/{id}/image")
    suspend fun getTeamLogo(
        @Path("id") id: String
    ) : ResponseBody

    @GET("tournament/{id}/image")
    suspend fun getTournamentLogo(
        @Path("id") id: String
    ) : ResponseBody
}