package com.example.acapi.routes

import com.example.acapi.module
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class CritterRoutesTest {

    @Test
    fun `GET critters filtered by fish only returns fish`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/critters?type=fish")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("coelacanth"))
        assertTrue(body.contains("sea-bass"))
        assertTrue(!body.contains("goliath-beetle"))
    }

    @Test
    fun `GET critters filtered by hemisphere and month`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/critters?type=bug&month=7&hemisphere=northern")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("goliath-beetle"))
        assertTrue(!body.contains("emperor-butterfly"))
    }

    @Test
    fun `GET critter by id returns detail`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/critters/coelacanth")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("\"name\":\"Coelacanth\""))
    }

    @Test
    fun `GET critters with unknown type returns 400`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/critters?type=dragon")
        assertEquals(HttpStatusCode.BadRequest, response.status)
    }
}