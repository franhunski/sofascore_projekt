package com.example.sofascore_zavrsni_projekt.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.sofascore_zavrsni_projekt.data.miniSofa_models.Sport
import com.example.sofascore_zavrsni_projekt.data.remote.Result
import com.example.sofascore_zavrsni_projekt.data.repository.MiniSofaRepository
import kotlinx.coroutines.launch

class SportViewModel(application: Application): AndroidViewModel(application) {
    private val miniSofaRepository = MiniSofaRepository(application)

    private val _sports = MutableLiveData<List<Sport>>().apply {
        viewModelScope.launch {
            value = when (val result = miniSofaRepository.getAllSports()) {
                is Result.Success -> {
                    result.data.sports
                }

                is Result.Error -> {
                    emptyList()
                }
            }
        }
    }
    val sports: LiveData<List<Sport>> = _sports
}