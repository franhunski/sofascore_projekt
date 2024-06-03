package com.example.sofascore_zavrsni_projekt.data.repository

import android.app.Application
import com.example.sofascore_zavrsni_projekt.data.local.MiniSofaDatabase
import com.example.sofascore_zavrsni_projekt.data.remote.Network
import com.example.sofascore_zavrsni_projekt.util.safeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MiniSofaRepository(application: Application) {

    private val api = Network.getInstance()
    private val miniSofaDao = MiniSofaDatabase.getInstance(application).miniSofaDao()

    suspend fun getAllSports() =
        withContext(Dispatchers.IO) {
            safeResponse {
                api.getSports()
            }
        }
}