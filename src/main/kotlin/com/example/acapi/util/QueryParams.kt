package com.example.acapi.util

import com.example.acapi.model.InvalidParameterException
import io.ktor.http.Parameters
import io.ktor.http.encodeURLQueryComponent

fun Parameters.intOrNull(name: String): Int? {
    val raw = this[name] ?: return null
    return raw.toIntOrNull() ?: throw InvalidParameterException("$name must be an integer, got '$raw'")
}

fun Parameters.pageParam(): Int = intOrNull("page") ?: 1

fun Parameters.pageSizeParam(): Int = intOrNull("page_size") ?: DEFAULT_PAGE_SIZE

fun Parameters.stringOrNull(name: String): String? =
    this[name]?.takeIf { it.isNotBlank() }?.lowercase()

fun Parameters.rebuildQueryString(): String = entries()
    .flatMap { (k, values) -> values.map { v -> "${k.encodeURLQueryComponent()}=${v.encodeURLQueryComponent()}" } }
    .joinToString("&")