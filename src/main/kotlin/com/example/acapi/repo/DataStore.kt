package com.example.acapi.repo

import com.example.acapi.model.Critter
import com.example.acapi.model.GameInfo
import com.example.acapi.model.Item
import com.example.acapi.model.MuseumEntry
import com.example.acapi.model.Villager
import kotlinx.serialization.json.Json

class DataStore(
    val villagers: List<Villager>,
    val critters: List<Critter>,
    val museum: List<MuseumEntry>,
    val items: List<Item>,
    val games: List<GameInfo>,
) {
    companion object {
        private val json = Json {
            ignoreUnknownKeys = true
            isLenient = false
        }

        fun load(): DataStore {
            return DataStore(
                villagers = readList("data/villagers.json"),
                critters = readList("data/critters.json"),
                museum = readList("data/museum.json"),
                items = readList("data/items.json"),
                games = readList("data/games.json"),
            )
        }

        private inline fun <reified T> readList(resourcePath: String): List<T> {
            val stream = this::class.java.classLoader.getResourceAsStream(resourcePath)
                ?: error("Missing data resource: $resourcePath")
            val text = stream.bufferedReader().use { it.readText() }
            return json.decodeFromString(text)
        }
    }
}