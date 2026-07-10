package com.example.acapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.acapp.di.AppModule
import com.example.acapp.ui.VillagerDetailScreen
import com.example.acapp.ui.VillagersListScreen

@Composable
fun App() {
    MaterialTheme {
        val nav = rememberNavController()
        NavHost(navController = nav, startDestination = "villagers") {
            composable("villagers") {
                VillagersListScreen(
                    repo = AppModule.villagerRepository,
                    onVillagerClick = { id -> nav.navigate("villagers/$id") },
                )
            }
            composable("villagers/{id}") { entry ->
                val id = entry.arguments?.getString("id").orEmpty()
                VillagerDetailScreen(
                    repo = AppModule.villagerRepository,
                    villagerId = id,
                    onBack = { nav.popBackStack() },
                )
            }
        }
    }
}
