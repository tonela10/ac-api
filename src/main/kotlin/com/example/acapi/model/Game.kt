package com.example.acapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class Game {
    @SerialName("acgc") ACGC,
    @SerialName("ww") WW,
    @SerialName("cf") CF,
    @SerialName("nl") NL,
    @SerialName("nh") NH,
    @SerialName("pg") PG;

    companion object {
        fun fromCode(code: String): Game? = entries.firstOrNull {
            it.name.equals(code, ignoreCase = true)
        }
    }
}

@Serializable
data class GameInfo(
    val code: String,
    val name: String,
    val platform: String,
    @SerialName("release_year") val releaseYear: Int,
)