package com.example.acapp.data

import com.example.acapp.data.dto.Villager

interface VillagerRepository {
    suspend fun listVillagers(): List<Villager>
    suspend fun getVillager(id: String): Villager
}

class RemoteVillagerRepository(private val api: AcApiClient) : VillagerRepository {
    override suspend fun listVillagers(): List<Villager> =
        api.listVillagers(pageSize = 100).data

    override suspend fun getVillager(id: String): Villager =
        api.getVillager(id)
}
