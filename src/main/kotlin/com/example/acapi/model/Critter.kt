package com.example.acapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class CritterType {
    @SerialName("fish") FISH,
    @SerialName("bug") BUG,
    @SerialName("sea_creature") SEA_CREATURE;
}

@Serializable
enum class Hemisphere {
    @SerialName("northern") NORTHERN,
    @SerialName("southern") SOUTHERN;
}

@Serializable
data class MonthAvailability(
    @SerialName("northern") val northern: List<Int> = emptyList(),
    @SerialName("southern") val southern: List<Int> = emptyList(),
)

@Serializable
data class Critter(
    val id: String,
    val name: String,
    val type: CritterType,
    val location: String,
    val price: Int,
    @SerialName("shadow_size") val shadowSize: String? = null,
    @SerialName("time_of_day") val timeOfDay: String? = null,
    val months: MonthAvailability = MonthAvailability(),
    val rarity: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    val games: List<Game> = emptyList(),
)
