package com.example.acapi.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val page: Int,
    @SerialName("page_size") val pageSize: Int,
    val total: Int,
    @SerialName("total_pages") val totalPages: Int,
    val next: String? = null,
    val prev: String? = null,
)

@Serializable
data class PagedResponse<T>(
    val data: List<T>,
    val pagination: Pagination,
)
