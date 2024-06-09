package com.example.sofascore_zavrsni_projekt.ui.tournament_details

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.liveData
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Tournament
import com.example.sofascore_zavrsni_projekt.data.remote.Result
import com.example.sofascore_zavrsni_projekt.data.repository.MiniSofaRepository
import com.example.sofascore_zavrsni_projekt.ui.adapter.StandingsItem
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import java.io.InputStream

class TournamentDetailsViewModel(application: Application): AndroidViewModel(application) {

    private val miniSofaRepository = MiniSofaRepository(application)

    private val _tournamentLogo = MutableLiveData<Bitmap>()
    val tournamentLogo: LiveData<Bitmap> get() = _tournamentLogo

    private val _tournamentDetails = MutableLiveData<Tournament>()
    val tournamentDetails: LiveData<Tournament> get() = _tournamentDetails

    private val _tournamentStandings = MutableLiveData<List<StandingsItem>>()
    val tournamentStandings: LiveData<List<StandingsItem>> get() = _tournamentStandings


    val liveData = Pager(
        PagingConfig(pageSize = ITEMS_PER_PAGE)
    ) {
        MatchesPagingSource(miniSofaRepository, 1)
    }.liveData.cachedIn(viewModelScope)


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

    fun fetchTournamentDetails(id: Int) {
        viewModelScope.launch {
            _tournamentDetails.value = when (val result = miniSofaRepository.getTournamentDetails(id)) {
                is Result.Success -> {
                    result.data
                }
                is Result.Error -> {
                    throw IllegalArgumentException("Failed to fetch tournament details for tournament with ID: $id")
                }

            }
        }
    }
    fun fetchTournamentStandings(id: Int) {
        viewModelScope.launch {
            val listOfStandings = mutableListOf<StandingsItem>()
            _tournamentStandings.value = when (val result = miniSofaRepository.getTournamentStandings(id)) {
                is Result.Success -> {
                    var position = 1
                    //listOfStandings.add(StandingsItem.StandingsHeaderItem("Team", result.data[0].tournament.sport.slug))
                    for (standings in result.data) {
                        for (standingRow in standings.sortedStandingsRows) {
                            listOfStandings.add(StandingsItem.StandingsInfoItem(standingRow, result.data[0].tournament.sport.slug, position))
                            position++
                        }
                    }
                    Log.d("TournamentStandings", "list=${result.data}")
                    listOfStandings
                }
                is Result.Error -> {
                    throw IllegalArgumentException("Failed to fetch tournament standings for tournament with ID: $id")
                }

            }
        }
    }
}