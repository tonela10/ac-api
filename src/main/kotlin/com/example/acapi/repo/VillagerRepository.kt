package com.example.acapi.repo

import com.example.acapi.model.Game
import com.example.acapi.model.Villager

class VillagerRepository(private val all: List<Villager>) {

    fun findById(id: String): Villager? = all.firstOrNull { it.id.equals(id, ignoreCase = true) }

    fun list(
        species: String? = null,
        personality: String? = null,
        gender: String? = null,
        game: Game? = null,
        birthdayMonth: Int? = null,
        hobby: String? = null,
        zodiac: String? = null,
    ): List<Villager> = all.filter { v ->
        (species == null || v.species.equals(species, ignoreCase = true)) &&
        (personality == null || v.personality.equals(personality, ignoreCase = true)) &&
        (gender == null || v.gender.equals(gender, ignoreCase = true)) &&
        (game == null || game in v.games) &&
        (birthdayMonth == null || v.birthdayMonth == birthdayMonth) &&
        (hobby == null || v.hobby?.equals(hobby, ignoreCase = true) == true) &&
        (zodiac == null || v.zodiac?.equals(zodiac, ignoreCase = true) == true)
    }
}