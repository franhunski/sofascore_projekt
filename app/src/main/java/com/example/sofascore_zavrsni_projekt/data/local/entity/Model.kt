package com.example.sofascore_zavrsni_projekt.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Sport(
    @PrimaryKey(autoGenerate = true)
    val externalId: Int = 0,
    val name: String,
    val slug: String
)