package com.example.acapi.repo

import com.example.acapi.model.Critter
import com.example.acapi.model.CritterType
import com.example.acapi.model.Game
import com.example.acapi.model.Hemisphere

class CritterRepository(private val all: List<Critter>) {

    fun findById(id: String): Critter? = all.firstOrNull { it.id.equals(id, ignoreCase = true) }

    fun list(
        type: CritterType? = null,
        game: Game? = null,
        location: String? = null,
        month: Int? = null,
        hemisphere: Hemisphere? = null,
        timeOfDay: String? = null,
        minPrice: Int? = null,
        maxPrice: Int? = null,
        shadowSize: String? = null,
        rarity: String? = null,
    ): List<Critter> = all.filter { c ->
        (type == null || c.type == type) &&
        (game == null || game in c.games) &&
        (location == null || c.location.equals(location, ignoreCase = true)) &&
        (month == null || matchesMonth(c, month, hemisphere)) &&
        (timeOfDay == null || c.timeOfDay?.equals(timeOfDay, ignoreCase = true) == true) &&
        (minPrice == null || c.price >= minPrice) &&
        (maxPrice == null || c.price <= maxPrice) &&
        (shadowSize == null || c.shadowSize?.equals(shadowSize, ignoreCase = true) == true) &&
        (rarity == null || c.rarity?.equals(rarity, ignoreCase = true) == true)
    }

    private fun matchesMonth(critter: Critter, month: Int, hemisphere: Hemisphere?): Boolean {
        return when (hemisphere) {
            Hemisphere.NORTHERN -> month in critter.months.northern
            Hemisphere.SOUTHERN -> month in critter.months.southern
            null -> month in critter.months.northern || month in critter.months.southern
        }
    }
}