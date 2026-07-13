package com.example.acapp.data

import com.example.acapp.data.dto.Villager
import com.example.acapp.platform.httpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val DATA_HOST = "tonela10.github.io"

class AcApiClient {
    private val client: HttpClient = HttpClient(httpClientEngine()) {
        expectSuccess = true

        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }

        install(HttpTimeout) {
            connectTimeoutMillis = 15_000
            requestTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }

        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    println("[ac-api] $message")
                }
            }
            level = LogLevel.INFO
        }

        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = DATA_HOST
            }
            header(HttpHeaders.Accept, "application/json")
            header(HttpHeaders.UserAgent, "ac-api-app/0.1 (KMP demo)")
        }
    }

    suspend fun fetchVillagers(): List<Villager> =
        client.get { url { path("ac-api", "data", "villagers.json") } }.body()
}
