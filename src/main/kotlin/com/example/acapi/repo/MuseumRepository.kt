package com.example.acapi.repo

import com.example.acapi.model.Game
import com.example.acapi.model.MuseumCategory
import com.example.acapi.model.MuseumEntry

class MuseumRepository(private val all: List<MuseumEntry>) {

    fun findById(id: String): MuseumEntry? = all.firstOrNull { it.id.equals(id, ignoreCase = true) }

    fun list(
        category: MuseumCategory? = null,
        game: Game? = null,
    ): List<MuseumEntry> = all.filter { m ->
        (category == null || m.category == category) &&
        (game == null || game in m.games)
    }
}