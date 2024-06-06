package com.example.sofascore_zavrsni_projekt.ui

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sofascore_zavrsni_projekt.data.remote.Result
import com.example.sofascore_zavrsni_projekt.data.repository.MiniSofaRepository
import com.example.sofascore_zavrsni_projekt.ui.adapter.EventItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import java.io.InputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SportViewModel(application: Application): AndroidViewModel(application) {

    private val miniSofaRepository = MiniSofaRepository(application)
    private val _eventInfo = MutableLiveData<List<EventItem>>()
    val eventInfo: LiveData<List<EventItem>> = _eventInfo

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    @RequiresApi(Build.VERSION_CODES.O)
    fun fetchEventsForSportAndDate(slug: String, date: String) {
        viewModelScope.launch {
            _isLoading.value = true
            var success: Boolean = false
            val eventItems : MutableMap<EventItem, MutableList<EventItem>> = mutableMapOf()
            _eventInfo.value = when (val result = miniSofaRepository.getAllEventsForSportAndDate(slug, date)) {
                is Result.Success -> {
                    success = true
                    val eventItemsList = mutableListOf<EventItem>()
                    val keys = mutableListOf<Int>()
                    val list = result.data
                    val listOfTournamentIds = mutableSetOf<Int>()
                    val listOfTeamIds = mutableSetOf<Int>()
                    for (event in list) {
                        listOfTournamentIds.add(event.tournament.id)
                        listOfTeamIds.add(event.homeTeam.id)
                        listOfTeamIds.add(event.awayTeam.id)
                    }
                    val tournamentLogos = async {  returnTournamentLogos(listOfTournamentIds) }
                    val teamLogos = async {  returnTeamLogos(listOfTeamIds) }
                    val mapOfTournamentLogos = tournamentLogos.await()
                    val mapOfTeamLogos = teamLogos.await()
                    if (list.isNotEmpty()) {
                        for (event in list) {
                            if (!keys.contains(event.tournament.id)) {
                                keys.add(event.tournament.id)
                                val list1 =
                                    mutableListOf(mapOfTeamLogos[event.homeTeam.id], mapOfTeamLogos[event.awayTeam.id]).let {
                                        EventItem.EventInfoItem(event,
                                            it
                                        )
                                    }.let { mutableListOf<EventItem>(it) }
                                eventItems[mapOfTournamentLogos[event.tournament.id]?.let {
                                    EventItem.HeaderItem(event.tournament,
                                        it
                                    )
                                }!!] = list1
                            } else {
                                eventItems[mapOfTournamentLogos[event.tournament.id]?.let {
                                    EventItem.HeaderItem(event.tournament,
                                        it
                                    )
                                }!!]?.add(EventItem.EventInfoItem(event, mutableListOf(mapOfTeamLogos[event.homeTeam.id], mapOfTeamLogos[event.awayTeam.id])))
                            }
                        }
                        val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
                        for ((_, events) in eventItems) {
                            events.sortBy {
                                val eventItem = it as EventItem.EventInfoItem
                                LocalDateTime.parse(eventItem.eventInfo.startDate, formatter)
                            }
                        }
                        for ((key, events) in eventItems) {
                            eventItemsList.add(key)
                            eventItemsList.addAll(events)
                        }

                        Log.d("mapa", "mapa=${eventItemsList}")

                    } else {
                        eventItemsList.add(EventItem.HeaderNoEventsItem("There are no events today!"))
                        Log.d("lista", "lista=${eventItemsList}")
                    }
                    eventItemsList
                }

                is Result.Error -> {
                    emptyList()
                }
            }
            Log.d("eventovi", "sports=${_eventInfo.value}")
            if (success) {
                _isLoading.value = false
            }

        }

    }


    private suspend fun returnTeamLogos(listOfLogos: MutableSet<Int>) : MutableMap<Int, Bitmap> {
        val mapOfTeamLogos = mutableMapOf<Int, Bitmap>()

        coroutineScope {
            for (id in listOfLogos) {
                val logoDeferred = async(Dispatchers.IO) {
                    when (val result = miniSofaRepository.getLogoOfTeam(id)) {
                        is Result.Success -> result.data
                        is Result.Error -> throw IllegalArgumentException("Failed to fetch logo for team with ID: $id")
                    }
                }
                val logo = logoDeferred.await()

                withContext(Dispatchers.IO) {
                    mapOfTeamLogos[id] = responseBodyToBitmap(logo)
                }

            }

        }
        return mapOfTeamLogos
    }

    private suspend fun returnTournamentLogos(listOfIds: MutableSet<Int>) : MutableMap<Int, Bitmap> {
        val mapOfTournamentLogos = mutableMapOf<Int, Bitmap>()
        coroutineScope {
            for (id in listOfIds) {
                val logoTournamentDeferred = async(Dispatchers.IO) {
                    when (val result = miniSofaRepository.getLogoOfTournament(id)) {
                        is Result.Success -> result.data
                        is Result.Error -> throw IllegalArgumentException("Failed to fetch logo for team with ID: $id")
                    }
                }
                val logo =  logoTournamentDeferred.await()
                withContext(Dispatchers.IO) {
                    mapOfTournamentLogos[id] = responseBodyToBitmap(logo)
                }
            }
        }
        return mapOfTournamentLogos
    }
    private fun responseBodyToBitmap(responseBody: ResponseBody): Bitmap {
        return try {
            val inputStream: InputStream = responseBody.byteStream()
            BitmapFactory.decodeStream(inputStream)
        } catch (e: Exception) {
            throw IllegalArgumentException("Failed to convert logo.")
        } finally {
            responseBody.close()
        }
    }

}