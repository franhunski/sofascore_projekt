package com.example.sofascore_zavrsni_projekt.data.repository

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.example.sofascore_zavrsni_projekt.data.local.MiniSofaDatabase
import com.example.sofascore_zavrsni_projekt.data.local.entity.Country
import com.example.sofascore_zavrsni_projekt.data.local.entity.Event
import com.example.sofascore_zavrsni_projekt.data.local.entity.Sport
import com.example.sofascore_zavrsni_projekt.data.local.entity.Team
import com.example.sofascore_zavrsni_projekt.data.local.entity.Tournament
import com.example.sofascore_zavrsni_projekt.data.remote.Network
import com.example.sofascore_zavrsni_projekt.util.safeResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.sofascore_zavrsni_projekt.data.remote.Result
import kotlinx.coroutines.async

class MiniSofaRepository(application: Application) {

    private val api = Network.getInstance()

    private val miniSofaDao = MiniSofaDatabase.getInstance(application).miniSofaDao()
    private val db = MiniSofaDatabase.getInstance(application)



    suspend fun getAllEventsForSportAndDate(slug: String, date: String)  =
        withContext(Dispatchers.IO) {
            Log.d("MiniSofaRepository", "Fetching events data")
            val response = safeResponse {
                api.getEventsForSportAndDate(slug, date)
            }
            when (response) {
                is Result.Success -> {
                    Log.d("MiniSofaRepository", "Response received: ${response.data}")

                    db.runInTransaction {
                        for (event in response.data) {
                            val events = Event(
                                externalId = event.id,
                                slug = event.slug,
                                homeTeamId = event.homeTeam.id,
                                awayTeamId = event.awayTeam.id,
                                awayScore = event.awayScore.total,
                                homeScore = event.homeScore.total,
                                round = event.round,
                                startDate = event.startDate,
                                status = event.status,
                                tournamentId = event.tournament.id,
                                winnerCode = event.winnerCode
                            )
                            val tournament = Tournament(
                                name = event.tournament.name,
                                slug = event.tournament.slug,
                                countryId = event.tournament.country.id,
                                externalId = event.tournament.id,
                                sportId = 1
                            )
                            val tournamentCountry = Country(
                                name = event.tournament.country.name,
                                externalId = event.tournament.country.id
                            )
                            val homeTeam = Team(
                                name = event.homeTeam.name,
                                externalId = event.homeTeam.id,
                                countryId = event.homeTeam.country.id,
                                managerName = "",
                                venue = ""
                            )
                            val awayTeam = Team(
                                name = event.awayTeam.name,
                                externalId = event.awayTeam.id,
                                countryId = event.awayTeam.country.id,
                                managerName = "",
                                venue = ""
                            )
                            val homeTeamCountry = Country(
                                name = event.homeTeam.country.name,
                                externalId = event.homeTeam.country.id
                            )
                            val awayTeamCountry = Country(
                                name = event.awayTeam.country.name,
                                externalId = event.awayTeam.country.id
                            )
                            val save = async {
                                miniSofaDao.insertCountry(tournamentCountry)
                                miniSofaDao.insertCountry(homeTeamCountry)
                                miniSofaDao.insertCountry(awayTeamCountry)
                                miniSofaDao.insertSport(Sport(name = "Football", slug = "football", externalId = 1))
                                miniSofaDao.insertTournament(tournament)
                                miniSofaDao.insertTeam(homeTeam)
                                miniSofaDao.insertTeam(awayTeam)
                                miniSofaDao.insertEventInfoList(events)
                            }

                        }
                    }
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

    suspend fun getEventWithId(id: Int)  =
        withContext(Dispatchers.IO) {
            Log.d("MiniSofaRepository event-id", "Fetching event data")
            val response = safeResponse {
                api.getEventWithId(id.toString())
            }
            when (response) {
                is Result.Success -> {
                    Log.d("MiniSofaRepository event-id", "Response received: ${response.data}")
                    Result.Success(response.data)
                }
                is Result.Error -> {
                    Log.e("MiniSofaRepository event-id", "Error fetching events data: ${response.error}")
                    response
                }
            }
        }
    suspend fun getEventIncidents(id: Int)  =
        withContext(Dispatchers.IO) {
            Log.d("MiniSofaRepository event-incident", "Fetching event incidents data")
            val response = safeResponse {
                api.getEventIncidents(id.toString())
            }
            when (response) {
                is Result.Success -> {
                    Log.d("MiniSofaRepository event-incidents", "Response received: ${response.data}")
                    Result.Success(response.data)
                }
                is Result.Error -> {
                    Log.e("MiniSofaRepository event-incidents", "Error fetching events data: ${response.error}")
                    response
                }
            }
        }

    suspend fun getTournamentDetails(id: Int)  =
        withContext(Dispatchers.IO) {
            Log.d("MiniSofaRepository tournament-details", "Fetching tournament details data")
            val response = safeResponse {
                api.getTournamentDetails(id.toString())
            }
            when (response) {
                is Result.Success -> {
                    Log.d("MiniSofaRepository tournament-details", "Response received: ${response.data}")
                    Result.Success(response.data)
                }
                is Result.Error -> {
                    Log.e("MiniSofaRepository tournament-details", "Error fetching tournament-details: ${response.error}")
                    response
                }
            }
        }

    suspend fun getTournamentEvents(id: Int, span: String, page: Int)  =
        withContext(Dispatchers.IO) {
            Log.d("MiniSofaRepository tournament-events", "Fetching tournament events data")
            val response = safeResponse {
                api.getTournamentEvents(id.toString(), span, page.toString())
            }
            when (response) {
                is Result.Success -> {
                    Log.d("MiniSofaRepository tournament-events", "Response received: ${response.data}")
                    Result.Success(response.data)
                }
                is Result.Error -> {
                    Log.e("MiniSofaRepository tournament-events", "Error fetching tournament-events: ${response.error}")
                    response
                }
            }
        }

    suspend fun getTournamentStandings(id: Int)  =
        withContext(Dispatchers.IO) {
            Log.d("MiniSofaRepository tournament-standings", "Fetching tournament standings data")
            val response = safeResponse {
                api.getTournamentStandings(id.toString())
            }
            when (response) {
                is Result.Success -> {
                    Log.d("MiniSofaRepository tournament-standings", "Response received: ${response.data}")
                    Result.Success(response.data)
                }
                is Result.Error -> {
                    Log.e("MiniSofaRepository tournament-standings", "Error fetching tournament-standings: ${response.error}")
                    response
                }
            }
        }
}