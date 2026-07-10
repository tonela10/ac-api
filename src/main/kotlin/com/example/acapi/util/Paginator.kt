package com.example.acapi.util

import com.example.acapi.model.InvalidParameterException
import com.example.acapi.model.PagedResponse
import com.example.acapi.model.Pagination

const val DEFAULT_PAGE_SIZE = 50
const val MAX_PAGE_SIZE = 200

fun <T> paginate(
    all: List<T>,
    page: Int,
    pageSize: Int,
    basePath: String,
    queryString: String,
): PagedResponse<T> {
    if (page < 1) throw InvalidParameterException("page must be >= 1, got $page")
    if (pageSize < 1 || pageSize > MAX_PAGE_SIZE) {
        throw InvalidParameterException("page_size must be between 1 and $MAX_PAGE_SIZE, got $pageSize")
    }

    val total = all.size
    val totalPages = if (total == 0) 0 else (total + pageSize - 1) / pageSize
    val fromIndex = ((page - 1) * pageSize).coerceAtMost(total)
    val toIndex = (fromIndex + pageSize).coerceAtMost(total)
    val slice = all.subList(fromIndex, toIndex)

    val nextLink = if (page < totalPages) buildLink(basePath, queryString, page + 1, pageSize) else null
    val prevLink = if (page > 1) buildLink(basePath, queryString, page - 1, pageSize) else null

    return PagedResponse(
        data = slice,
        pagination = Pagination(
            page = page,
            pageSize = pageSize,
            total = total,
            totalPages = totalPages,
            next = nextLink,
            prev = prevLink,
        ),
    )
}

private fun buildLink(basePath: String, queryString: String, page: Int, pageSize: Int): String {
    val params = queryString
        .split('&')
        .filter { it.isNotEmpty() && !it.startsWith("page=") && !it.startsWith("page_size=") }
        .toMutableList()
    params += "page=$page"
    params += "page_size=$pageSize"
    return "$basePath?${params.joinToString("&")}"
}