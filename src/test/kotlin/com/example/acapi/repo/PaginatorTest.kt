package com.example.acapi.repo

import com.example.acapi.model.InvalidParameterException
import com.example.acapi.util.paginate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PaginatorTest {

    private val items = (1..25).map { "item-$it" }

    @Test
    fun `first page returns first page-size items`() {
        val result = paginate(items, page = 1, pageSize = 10, basePath = "/x", queryString = "")
        assertEquals(10, result.data.size)
        assertEquals("item-1", result.data.first())
        assertEquals(25, result.pagination.total)
        assertEquals(3, result.pagination.totalPages)
        assertNull(result.pagination.prev)
        assertTrue(result.pagination.next!!.contains("page=2"))
    }

    @Test
    fun `last page is partial`() {
        val result = paginate(items, page = 3, pageSize = 10, basePath = "/x", queryString = "")
        assertEquals(5, result.data.size)
        assertEquals("item-21", result.data.first())
        assertNull(result.pagination.next)
    }

    @Test
    fun `page beyond total is empty`() {
        val result = paginate(items, page = 100, pageSize = 10, basePath = "/x", queryString = "")
        assertTrue(result.data.isEmpty())
        assertEquals(3, result.pagination.totalPages)
    }

    @Test
    fun `invalid page throws`() {
        assertFailsWith<InvalidParameterException> {
            paginate(items, page = 0, pageSize = 10, basePath = "/x", queryString = "")
        }
    }

    @Test
    fun `invalid page_size throws`() {
        assertFailsWith<InvalidParameterException> {
            paginate(items, page = 1, pageSize = -1, basePath = "/x", queryString = "")
        }
        assertFailsWith<InvalidParameterException> {
            paginate(items, page = 1, pageSize = 500, basePath = "/x", queryString = "")
        }
    }

    @Test
    fun `next link preserves other query params`() {
        val result = paginate(items, page = 1, pageSize = 10, basePath = "/x", queryString = "species=cat&page=1")
        val next = result.pagination.next!!
        assertTrue(next.contains("species=cat"), "expected species param preserved, got: $next")
        assertTrue(next.contains("page=2"))
        assertTrue(!next.contains("page=1"))
    }
}