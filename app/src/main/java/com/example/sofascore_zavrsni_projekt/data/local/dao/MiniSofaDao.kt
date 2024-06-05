package com.example.sofascore_zavrsni_projekt.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sofascore_zavrsni_projekt.data.local.entity.Sport
import com.example.sofascore_zavrsni_projekt.data.local.entity.Event

@Dao
interface MiniSofaDao {

    @Query("SELECT * FROM Sport")
    fun getAllSports() : LiveData<List<Sport>>

    @Insert
    suspend fun insertSport(sport: Sport)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvent(event: Event)

    @Query("SELECT * FROM Event")
    fun getAllEventsForSportAndDate(): LiveData<List<Event>>
}