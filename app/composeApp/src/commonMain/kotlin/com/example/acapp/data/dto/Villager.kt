package com.example.acapp.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Villager(
    val id: String,
    val name: String,
    val species: String,
    val personality: String,
    val gender: String,
    @SerialName("birthday_month") val birthdayMonth: Int,
    @SerialName("birthday_day") val birthdayDay: Int,
    val zodiac: String? = null,
    val catchphrase: String? = null,
    val hobby: String? = null,
    @SerialName("image_url") val imageUrl: String? = null,
    val games: List<String> = emptyList(),
)
