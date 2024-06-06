package com.example.sofascore_zavrsni_projekt.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(indices = [Index(value = ["externalId"], unique = true)])
data class Country(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val externalId: Int
)

@Entity(foreignKeys = [
    ForeignKey(
        entity = Team::class,
        parentColumns = ["externalId"],
        childColumns = ["homeTeamId"],
        onDelete = ForeignKey.CASCADE),
    ForeignKey(
        entity = Team::class,
        parentColumns = ["externalId"],
        childColumns = ["awayTeamId"],
        onDelete = ForeignKey.CASCADE),
    ForeignKey(
        entity = Tournament::class,
        parentColumns = ["externalId"],
        childColumns = ["tournamentId"],
        onDelete = ForeignKey.CASCADE)
    ], indices = [Index(value = ["externalId"], unique = true)])
data class Event(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val externalId: Int,
    val slug: String,
    val homeTeamId: Int,
    val awayTeamId: Int,
    val homeScore: Int,
    val awayScore: Int,
    val startDate: String,
    val tournamentId: Int,
    val round: Int,
    val winnerCode: String?,
    val status: String
)

@Entity(foreignKeys = [
    ForeignKey(
        entity = Country::class,
        parentColumns = ["externalId"],
        childColumns = ["countryId"],
        onDelete = ForeignKey.CASCADE)
    ], indices = [Index(value = ["externalId"], unique = true)])
data class Player(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val slug: String,
    val position: String,
    val externalId: Int,
    val countryId: Int
)

@Entity(indices = [Index(value = ["externalId"], unique = true)])
data class Sport(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val slug: String,
    val externalId: Int
)

@Entity(foreignKeys = [
    ForeignKey(
        entity = Country::class,
        parentColumns = ["externalId"],
        childColumns = ["countryId"],
        onDelete = ForeignKey.CASCADE)
    ], indices = [Index(value = ["externalId"], unique = true)])
data class Team(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val managerName: String,
    val venue: String,
    val externalId: Int,
    val countryId: Int
)

@Entity(foreignKeys = [
    ForeignKey(
        entity = Sport::class,
        parentColumns = ["externalId"],
        childColumns = ["sportId"],
        onDelete = ForeignKey.CASCADE),
    ForeignKey(
        entity = Country::class,
        parentColumns = ["externalId"],
        childColumns = ["countryId"],
        onDelete = ForeignKey.CASCADE)
    ], indices = [Index(value = ["externalId"], unique = true)])
data class Tournament(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val slug: String,
    val externalId: Int,
    val sportId: Int,
    val countryId: Int
)
