package com.example.acapi.repo

import com.example.acapi.model.Game
import com.example.acapi.model.Item
import com.example.acapi.model.ItemCategory

class ItemRepository(private val all: List<Item>) {

    fun findById(id: String): Item? = all.firstOrNull { it.id.equals(id, ignoreCase = true) }

    fun list(
        category: ItemCategory? = null,
        game: Game? = null,
        theme: String? = null,
        source: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        size: String? = null,
    ): List<Item> = all.filter { i ->
        (category == null || i.category == category) &&
        (game == null || game in i.games) &&
        (theme == null || i.theme?.equals(theme, ignoreCase = true) == true) &&
        (source == null || i.source?.equals(source, ignoreCase = true) == true) &&
        (minPrice == null || (i.buyPrice ?: 0) >= minPrice) &&
        (maxPrice == null || (i.buyPrice ?: Int.MAX_VALUE) <= maxPrice) &&
        (size == null || i.size?.equals(size, ignoreCase = true) == true)
    }
}