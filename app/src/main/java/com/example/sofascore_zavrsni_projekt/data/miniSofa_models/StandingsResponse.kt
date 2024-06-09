package com.example.sofascore_zavrsni_projekt.data.miniSofa_models

data class StandingsResponse(
    val id: Int,
    val tournament: Tournament,
    val type: String,
    val sortedStandingsRows: List<StandingsRow>
)

data class StandingsRow(
    val id: Int,
    val team: Team,
    val points: Int,
    val scoresFor: Int,
    val scoresAgainst: Int,
    val played: Int,
    val wins: Int,
    val draws: Int,
    val loses: Int,
    val percentage: Double
)