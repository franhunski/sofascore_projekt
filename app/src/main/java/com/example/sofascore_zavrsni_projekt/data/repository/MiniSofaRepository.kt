package com.example.sofascore_zavrsni_projekt.data.repository

import android.app.Application
import android.util.Log
import com.example.sofascore_zavrsni_projekt.data.local.MiniSofaDatabase
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Event
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Sport
import com.example.sofascore_zavrsni_projekt.data.remote.Network
import com.example.sofascore_zavrsni_projekt.util.safeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.sofascore_zavrsni_projekt.data.remote.Result

class MiniSofaRepository(application: Application) {

    private val api = Network.getInstance()
    //private val miniSofaDao = MiniSofaDatabase.getInstance(application).miniSofaDao()

    suspend fun getAllSports(): Result<List<Sport>> =
        withContext(Dispatchers.IO) {
            Log.d("SportRepository", "Fetching sports data")
            val response = safeResponse {
                api.getSports()
            }
            when (response) {
                is Result.Success -> {
                    Log.d("SportRepository", "Response received: ${response.data}")
                    Result.Success(response.data)
                }
                is Result.Error -> {
                    Log.e("SportRepository", "Error fetching sports data: ${response.error}")
                    response
                }
            }
        }

    suspend fun getAllEventsForSportAndDate(slug: String, date: String)  =
        withContext(Dispatchers.IO) {
            Log.d("MiniSofaRepository", "Fetching events data")
            val response = safeResponse {
                api.getEventsForSportAndDate(slug, date)
            }
            when (response) {
                is Result.Success -> {
                    Log.d("MiniSofaRepository", "Response received: ${response.data}")
                    Result.Success(response.data)
                }
                is Result.Error -> {
                    Log.e("MiniSofaRepository", "Error fetching events data: ${response.error}")
                    response
                }
            }
        }

    suspend fun getLogoOfTeam(id: Int)  =
        withContext(Dispatchers.IO) {
            Log.d("MiniSofaRepository team logo", "Fetching logo data")
            val response = safeResponse {
                api.getTeamLogo(id.toString())
            }
            when (response) {
                is Result.Success -> {
                    Log.d("MiniSofaRepository team logo", "Response received: ${response.data}")
                    Result.Success(response.data)
                }
                is Result.Error -> {
                    Log.e("MiniSofaRepository team logo", "Error fetching events data: ${response.error}")
                    response
                }
            }
        }

    suspend fun getLogoOfTournament(id: Int)  =
        withContext(Dispatchers.IO) {
            Log.d("MiniSofaRepository tournament logo", "Fetching logo data")
            val response = safeResponse {
                api.getTournamentLogo(id.toString())
            }
            when (response) {
                is Result.Success -> {
                    Log.d("MiniSofaRepository tournament logo", "Response received: ${response.data}")
                    Result.Success(response.data)
                }
                is Result.Error -> {
                    Log.e("MiniSofaRepository tournament logo", "Error fetching events data: ${response.error}")
                    response
                }
            }
        }
}