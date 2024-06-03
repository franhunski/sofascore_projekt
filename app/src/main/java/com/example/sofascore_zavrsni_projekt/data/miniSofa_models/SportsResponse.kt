package com.example.sofascore_zavrsni_projekt.data.miniSofa_models

import java.io.Serializable


data class SportsResponse(
    val sports: List<Sport>
) : Serializable

data class Sport(
    val id: Int,
    val name: String,
    val slug: String
) : Serializable