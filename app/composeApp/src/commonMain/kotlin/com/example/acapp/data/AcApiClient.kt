package com.example.acapp.data

import com.example.acapp.data.dto.Villager
import com.example.acapp.platform.httpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val DATA_HOST = "tonela10.github.io"
const val DATA_BASE_PATH = "/ac-api/data"

class AcApiClient {
    val client: HttpClient = HttpClient(httpClientEngine()) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
        defaultRequest {
            url {
                protocol = URLProtocol.HTTPS
                host = DATA_HOST
            }
        }
    }

    suspend fun fetchVillagers(): List<Villager> =
        client.get { url { path("ac-api", "data", "villagers.json") } }.body()
}
