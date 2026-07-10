package com.example.acapi.repo

import com.example.acapi.model.ItemCategory
import kotlin.test.Test
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class DataIntegrityTest {

    private val store = DataStore.load()

    @Test
    fun `all DIY recipe materials reference known items`() {
        val knownIds = store.items.map { it.id }.toSet()
        val recipes = store.items.filter { it.category == ItemCategory.DIY_RECIPE }
        assertTrue(recipes.isNotEmpty(), "seed data should include at least one DIY recipe")
        recipes.forEach { recipe ->
            recipe.materials.forEach { mat ->
                assertTrue(
                    mat.itemId in knownIds,
                    "recipe ${recipe.id} references unknown material ${mat.itemId}",
                )
            }
        }
    }

    @Test
    fun `every villager references known game codes`() {
        val knownGameCodes = store.games.map { it.code }.toSet()
        store.villagers.forEach { v ->
            v.games.forEach { g ->
                assertTrue(
                    g.name.lowercase() in knownGameCodes,
                    "villager ${v.id} references unknown game ${g.name}",
                )
            }
        }
    }

    @Test
    fun `every entity has non-empty id and name`() {
        store.villagers.forEach { assertTrue(it.id.isNotBlank() && it.name.isNotBlank()) }
        store.critters.forEach { assertTrue(it.id.isNotBlank() && it.name.isNotBlank()) }
        store.museum.forEach { assertTrue(it.id.isNotBlank() && it.name.isNotBlank()) }
        store.items.forEach { assertTrue(it.id.isNotBlank() && it.name.isNotBlank()) }
    }

    @Test
    fun `ids are unique within each resource`() {
        assertUnique(store.villagers.map { it.id }, "villagers")
        assertUnique(store.critters.map { it.id }, "critters")
        assertUnique(store.museum.map { it.id }, "museum")
        assertUnique(store.items.map { it.id }, "items")
    }

    @Test
    fun `data files were loaded`() {
        assertNotNull(store.villagers.firstOrNull())
        assertNotNull(store.critters.firstOrNull())
        assertNotNull(store.museum.firstOrNull())
        assertNotNull(store.items.firstOrNull())
        assertNotNull(store.games.firstOrNull())
    }

    private fun assertUnique(ids: List<String>, resource: String) {
        val dupes = ids.groupingBy { it }.eachCount().filter { it.value > 1 }
        assertTrue(dupes.isEmpty(), "$resource has duplicate ids: ${dupes.keys}")
    }
}