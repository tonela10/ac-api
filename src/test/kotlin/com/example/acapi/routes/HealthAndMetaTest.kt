package com.example.acapi.routes

import com.example.acapi.module
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class HealthAndMetaTest {

    @Test
    fun `GET health returns ok`() = testApplication {
        application { module() }
        val response = client.get("/health")
        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("""{"status":"ok"}""", response.bodyAsText())
    }

    @Test
    fun `GET games returns all six games`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/games")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        listOf("acgc", "ww", "cf", "nl", "nh", "pg").forEach {
            assertTrue(body.contains("\"$it\""), "expected game code $it in response")
        }
    }
}