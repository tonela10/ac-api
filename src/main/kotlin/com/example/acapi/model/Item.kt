package com.example.acapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class ItemCategory {
    @SerialName("furniture") FURNITURE,
    @SerialName("clothing") CLOTHING,
    @SerialName("tool") TOOL,
    @SerialName("diy_recipe") DIY_RECIPE,
    @SerialName("material") MATERIAL,
    @SerialName("other") OTHER;
}

@Serializable
data class RecipeMaterial(
    @SerialName("item_id") val itemId: String,
    val quantity: Int,
)

@Serializable
data class Item(
    val id: String,
    val name: String,
    val category: ItemCategory,
    @SerialName("buy_price") val buyPrice: Int? = null,
    @SerialName("sell_price") val sellPrice: Int? = null,
    val theme: String? = null,
    val source: String? = null,
    val size: String? = null,
    val materials: List<RecipeMaterial> = emptyList(),
    val games: List<Game> = emptyList(),
)
