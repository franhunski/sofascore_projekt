package com.example.sofascore_zavrsni_projekt.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sofascore_zavrsni_projekt.data.local.entity.Sport

@Dao
interface MiniSofaDao {

    @Query("SELECT * FROM Sport")
    fun getAllSports() : LiveData<List<Sport>>

    @Insert
    suspend fun insertSport(sport: Sport)
}