package com.example.acapp.data

import com.example.acapp.data.dto.Villager
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

interface VillagerRepository {
    suspend fun listVillagers(): List<Villager>
    suspend fun getVillager(id: String): Villager
}

class RemoteVillagerRepository(private val api: AcApiClient) : VillagerRepository {

    private val mutex = Mutex()
    private var cached: List<Villager>? = null

    override suspend fun listVillagers(): List<Villager> = mutex.withLock {
        cached ?: api.fetchVillagers().also { cached = it }
    }

    override suspend fun getVillager(id: String): Villager {
        val all = listVillagers()
        return all.firstOrNull { it.id.equals(id, ignoreCase = true) }
            ?: throw NoSuchElementException("Villager '$id' not found")
    }
}
