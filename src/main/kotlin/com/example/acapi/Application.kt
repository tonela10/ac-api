package com.example.acapi

import com.example.acapi.plugins.configureCallLogging
import com.example.acapi.plugins.configureCors
import com.example.acapi.plugins.configureSerialization
import com.example.acapi.plugins.configureStatusPages
import com.example.acapi.repo.CritterRepository
import com.example.acapi.repo.DataStore
import com.example.acapi.repo.ItemRepository
import com.example.acapi.repo.MuseumRepository
import com.example.acapi.repo.VillagerRepository
import com.example.acapi.routes.critterRoutes
import com.example.acapi.routes.healthRoutes
import com.example.acapi.routes.itemRoutes
import com.example.acapi.routes.metaRoutes
import com.example.acapi.routes.museumRoutes
import com.example.acapi.routes.villagerRoutes
import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module).start(wait = true)
}

fun Application.module() {
    val store = DataStore.load()
    val villagerRepo = VillagerRepository(store.villagers)
    val critterRepo = CritterRepository(store.critters)
    val museumRepo = MuseumRepository(store.museum)
    val itemRepo = ItemRepository(store.items)

    configureSerialization()
    configureStatusPages()
    configureCors()
    configureCallLogging()

    routing {
        healthRoutes()
        route("/api/v1") {
            villagerRoutes(villagerRepo)
            critterRoutes(critterRepo)
            museumRoutes(museumRepo)
            itemRoutes(itemRepo)
            metaRoutes(store)
        }
    }
}