package com.example.acapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class MuseumCategory {
    @SerialName("fossil") FOSSIL,
    @SerialName("art") ART;
}

@Serializable
data class MuseumEntry(
    val id: String,
    val name: String,
    val category: MuseumCategory,
    val price: Int? = null,
    @SerialName("part_of") val partOf: String? = null,
    @SerialName("real_artwork") val realArtwork: Boolean? = null,
    @SerialName("forgery_hint") val forgeryHint: String? = null,
    val artist: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    val games: List<Game> = emptyList(),
)
