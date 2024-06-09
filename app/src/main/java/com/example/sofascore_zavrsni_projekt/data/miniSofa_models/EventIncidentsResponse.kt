package com.example.sofascore_zavrsni_projekt.data.miniSofa_models


sealed class Incident {
    abstract val id: Int
    abstract val time: Int
    abstract val type: String

    data class Goal(
        val player: Player,
        val scoringTeam: String,
        val homeScore: Int,
        val awayScore: Int,
        val goalType: String,
        override val id: Int,
        override val time: Int,
        override val type: String
    ) : Incident()

    data class Card(
        val player: Player,
        val teamSide: String,
        val color: String,
        override val id: Int,
        override val time: Int,
        override val type: String
    ) : Incident()

    data class Period(
        val text: String,
        override val id: Int,
        override val time: Int,
        override val type: String
    ) : Incident()
}
data class Player(
    val id: Int,
    val name: String,
    val slug: String,
    val country: Country,
    val position: String
)