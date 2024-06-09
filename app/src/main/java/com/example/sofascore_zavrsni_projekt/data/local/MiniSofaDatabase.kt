package com.example.sofascore_zavrsni_projekt.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sofascore_zavrsni_projekt.data.local.dao.MiniSofaDao
import com.example.sofascore_zavrsni_projekt.data.local.entity.Country
import com.example.sofascore_zavrsni_projekt.data.local.entity.Event
import com.example.sofascore_zavrsni_projekt.data.local.entity.Player
import com.example.sofascore_zavrsni_projekt.data.local.entity.Sport
import com.example.sofascore_zavrsni_projekt.data.local.entity.Team
import com.example.sofascore_zavrsni_projekt.data.local.entity.Tournament

@Database(entities = [Country::class, Event::class, Player::class, Sport::class, Team::class, Tournament::class], version = 1)
abstract class MiniSofaDatabase : RoomDatabase() {
    abstract fun miniSofaDao(): MiniSofaDao

    companion object {
        private const val DATABASE_NAME = "miniSofa_App.db"

        @Volatile
        private var INSTANCE: MiniSofaDatabase? = null

        fun getInstance(context: Context): MiniSofaDatabase {

            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MiniSofaDatabase::class.java,
                        DATABASE_NAME
                    ).build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}