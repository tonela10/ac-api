package com.example.acapp.data

import com.example.acapp.data.dto.PagedResponse
import com.example.acapp.data.dto.Villager
import com.example.acapp.platform.httpClientEngine
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.URLProtocol
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val API_HOST = "ac.jolgorio.app"

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
                host = API_HOST
            }
        }
    }

    suspend fun listVillagers(pageSize: Int = 50): PagedResponse<Villager> =
        client.get {
            url { path("api", "v1", "villagers") }
            parameter("page_size", pageSize)
        }.body()

    suspend fun getVillager(id: String): Villager =
        client.get {
            url { path("api", "v1", "villagers", id) }
        }.body()
}
