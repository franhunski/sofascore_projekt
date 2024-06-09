package com.example.sofascore_zavrsni_projekt.data.remote

import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Event
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Incident
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Sport
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.StandingsResponse
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Tournament
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

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

    @GET("event/{id}")
    suspend fun getEventWithId(
        @Path("id") id: String
    ) : Event

    @GET("event/{id}/incidents")
    suspend fun getEventIncidents(
        @Path("id") id: String
    ) : List<Incident>

    @GET("tournament/{id}")
    suspend fun getTournamentDetails(
        @Path("id") id: String
    ) : Tournament

    @GET("tournament/{id}/events/{span}/{page}")
    suspend fun getTournamentEvents(
        @Path("id") id: String,
        @Path("span") span: String,
        @Path("page") page: String
    ) : List<Event>

    @GET("tournament/{id}/standings")
    suspend fun getTournamentStandings(
        @Path("id") id: String
    ) : List<StandingsResponse>
}