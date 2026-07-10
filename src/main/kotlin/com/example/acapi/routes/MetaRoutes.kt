package com.example.acapi.routes

import com.example.acapi.repo.DataStore
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import kotlinx.serialization.Serializable

@Serializable
private data class HealthResponse(val status: String)

fun Route.metaRoutes(store: DataStore) {
    get("/games") {
        call.respond(store.games)
    }
}

fun Route.healthRoutes() {
    get("/health") {
        call.respond(HealthResponse("ok"))
    }
}