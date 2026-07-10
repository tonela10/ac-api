package com.example.acapi.routes

import com.example.acapi.module
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ItemRoutesTest {

    @Test
    fun `GET items filters by category`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/items?category=diy_recipe")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("wedding-arch"))
        assertTrue(!body.contains("wooden-chair"))
    }

    @Test
    fun `GET items filters by theme`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/items?theme=wedding")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("wedding-arch"))
    }

    @Test
    fun `DIY recipe response includes materials`() = testApplication {
        application { module() }
        val response = client.get("/api/v1/items/wedding-arch")
        assertEquals(HttpStatusCode.OK, response.status)
        val body = response.bodyAsText()
        assertTrue(body.contains("\"materials\""))
        assertTrue(body.contains("\"item_id\":\"white-rose\""))
    }
}