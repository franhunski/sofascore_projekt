package com.example.sofascore_zavrsni_projekt.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sofascore_zavrsni_projekt.data.local.entity.Country
import com.example.sofascore_zavrsni_projekt.data.local.entity.Sport
import com.example.sofascore_zavrsni_projekt.data.local.entity.Event
import com.example.sofascore_zavrsni_projekt.data.local.entity.Team
import com.example.sofascore_zavrsni_projekt.data.local.entity.Tournament

@Dao
interface MiniSofaDao {

    @Query("SELECT * FROM Sport")
    fun getAllSports() : LiveData<List<Sport>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSport(sport: Sport)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTeam(team: Team)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTournament(tournament: Tournament)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEventInfoList(event: Event)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCountry(country: Country)

    @Query("SELECT * FROM Event WHERE slug = :slug AND DATE(startDate) = :date")
    fun getAllEventsForSportAndDate(slug: String, date: String): LiveData<List<Event>>
}