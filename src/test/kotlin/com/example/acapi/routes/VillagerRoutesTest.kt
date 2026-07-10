package com.example.acapi.routes

import com.example.acapi.module
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class VillagerRoutesTest {

    @Test
    fun `GET villagers returns paginated list`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/villagers?page_size=3")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("\"data\""))
        assertTrue(body.contains("\"pagination\""))
        assertTrue(body.contains("\"page_size\":3"))
    }

    @Test
    fun `GET villagers filters by species`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/villagers?species=cat")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("raymond"))
        assertTrue(body.contains("bob"))
        assertTrue(body.contains("kabuki"))
        assertTrue(!body.contains("audie"))
    }

    @Test
    fun `GET villagers filters by game`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/villagers?game=acgc")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("bob"))
        assertTrue(!body.contains("raymond"))
    }

    @Test
    fun `GET villager by id returns detail`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/villagers/raymond")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("\"name\":\"Raymond\""))
        assertTrue(body.contains("\"species\":\"cat\""))
    }

    @Test
    fun `GET unknown villager returns 404 with standard error shape`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/villagers/does-not-exist")
        assertEquals(HttpStatusCode.NotFound, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("\"code\":\"not_found\""))
    }

    @Test
    fun `GET villagers with page beyond total returns empty data`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/villagers?page=99999")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("\"data\":[]"))
    }

    @Test
    fun `GET villagers with invalid page_size returns 400`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/villagers?page_size=-1")
        assertEquals(HttpStatusCode.BadRequest, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("\"code\":\"invalid_parameter\""))
    }
}