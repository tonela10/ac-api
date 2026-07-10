package com.example.acapp.di

import com.example.acapp.data.AcApiClient
import com.example.acapp.data.RemoteVillagerRepository
import com.example.acapp.data.VillagerRepository

object AppModule {
    private val apiClient by lazy { AcApiClient() }
    val villagerRepository: VillagerRepository by lazy { RemoteVillagerRepository(apiClient) }
}
