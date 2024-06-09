package com.example.sofascore_zavrsni_projekt.ui.event_details

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Event
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Incident
import com.example.sofascore_zavrsni_projekt.data.remote.Result
import com.example.sofascore_zavrsni_projekt.data.repository.MiniSofaRepository
import com.example.sofascore_zavrsni_projekt.ui.adapter.IncidentItem
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.InputStream

class EventDetailsViewModel(application: Application): AndroidViewModel(application) {
    private val miniSofaRepository = MiniSofaRepository(application)

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _teamLogos = MutableLiveData<List<Bitmap>>()
    val teamLogos: LiveData<List<Bitmap>> = _teamLogos

    private val _tournamentLogo = MutableLiveData<Bitmap>()
    val tournamentLogo: LiveData<Bitmap> get() = _tournamentLogo

    private val _eventIncidents = MutableLiveData<List<IncidentItem>>()
    val eventIncidents: LiveData<List<IncidentItem>> = _eventIncidents

    fun fetchEventWithId(id: Int) {
        viewModelScope.launch {
            _event.value = when (val result = miniSofaRepository.getEventWithId(id)) {
                is Result.Success -> {
                    Log.d("eventId", "success")
                    result.data
                }

                is Result.Error -> {
                    Log.d("eventId", "failure${result.error}")
                    throw IllegalArgumentException("Failed to fetch event for $id")
                }

            }
        }
    }


    fun fetchTournamentLogo(id: Int) {
        viewModelScope.launch {
            _tournamentLogo.value = when (val result = miniSofaRepository.getLogoOfTournament(id)) {
                is Result.Success -> {
                    Log.d("eventId", "success")
                    responseBodyToBitmap(result.data)
                }

                is Result.Error -> {
                    Log.d("eventId", "failure${result.error}")
                    throw IllegalArgumentException("Failed to fetch event for $id")
                }

            }
        }
    }

    fun fetchTeamLogos(homeId: Int, awayId: Int) {
        val listOfLogos = mutableListOf<Bitmap>()
        viewModelScope.launch {
            when (val result = miniSofaRepository.getLogoOfTeam(homeId)) {
                is Result.Success -> {
                    Log.d("eventId", "success")
                    listOfLogos.add(responseBodyToBitmap(result.data))
                }

                is Result.Error -> {
                    Log.d("eventId", "failure${result.error}")
                    throw IllegalArgumentException("Failed to fetch event for $homeId")
                }
            }
            when (val result = miniSofaRepository.getLogoOfTeam(awayId)) {
                is Result.Success -> {
                    Log.d("eventId", "success")
                    listOfLogos.add(responseBodyToBitmap(result.data))
                }

                is Result.Error -> {
                    Log.d("eventId", "failure${result.error}")
                    throw IllegalArgumentException("Failed to fetch event for $awayId")
                }
            }
            _teamLogos.value = listOfLogos
        }
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

    fun fetchEventIncidents(id: Int, sport: String, tournamentId: Int) {
        viewModelScope.launch {
            _eventIncidents.value = when (val result = miniSofaRepository.getEventIncidents(id)) {
                is Result.Success -> {
                    val list = result.data
                    val incidentItemsList = mutableListOf<IncidentItem>()
                    if (list.isNotEmpty()) {
                        Log.d("incidentId", "success")
                        for (incident in list) {
                            when (incident) {
                                is Incident.Card -> {
                                    incidentItemsList.add(IncidentItem.CardIncidentItem(incident))
                                }
                                is Incident.Goal -> {
                                    incidentItemsList.add(IncidentItem.GoalIncidentItem(incident, sport))
                                }
                                is Incident.Period -> {
                                    //incidentItemsList.add(IncidentItem.PeriodIncidentItem(incident))
                                    event.value?.status?.let {
                                        IncidentItem.PeriodIncidentItem(incident,
                                            it
                                        )
                                    }?.let { incidentItemsList.add(it) }
                                }
                            }
                        }
                    } else {
                        incidentItemsList.add(IncidentItem.HeaderNoIncidentsItem("No results yet.", tournamentId))
                    }
                    incidentItemsList.reversed()
                }

                is Result.Error -> {
                    Log.d("tournamentId", "failure${result.error}")
                    throw IllegalArgumentException("Failed to fetch event incident for event with ID: $id")
                }
            }
        }
    }

}