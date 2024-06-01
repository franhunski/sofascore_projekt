package com.example.sofascore_zavrsni_projekt.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sofascore_zavrsni_projekt.data.local.dao.MiniSofaDao
import com.example.sofascore_zavrsni_projekt.data.local.entity.Sport

@Database(entities = [Sport::class], version = 1)
abstract class MiniSofaDatabase : RoomDatabase() {
    abstract fun miniSofaDao(): MiniSofaDao

    companion object {
        private const val DATABASE_NAME = "minisofa_app.db"

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