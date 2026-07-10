package com.example.acapi.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiError(val error: ErrorBody) {
    @Serializable
    data class ErrorBody(
        val code: String,
        val message: String,
        val status: Int,
    )

    companion object {
        fun notFound(message: String) =
            ApiError(ErrorBody("not_found", message, 404))

        fun invalidParameter(message: String) =
            ApiError(ErrorBody("invalid_parameter", message, 400))

        fun internal(message: String = "Internal server error") =
            ApiError(ErrorBody("internal_error", message, 500))
    }
}

class InvalidParameterException(message: String) : RuntimeException(message)
