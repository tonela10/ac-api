package com.example.acapp.util

fun Throwable.toUserFacingMessage(fallback: String): String =
    "${this::class.simpleName}: ${message ?: fallback}"
