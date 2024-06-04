package com.example.sofascore_zavrsni_projekt.data.repository

import android.app.Application
import android.util.Log
import com.example.sofascore_zavrsni_projekt.data.local.MiniSofaDatabase
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
}