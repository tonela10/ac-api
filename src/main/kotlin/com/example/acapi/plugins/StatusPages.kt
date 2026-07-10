package com.example.acapi.plugins

import com.example.acapi.model.ApiError
import com.example.acapi.model.InvalidParameterException
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("com.example.acapi.StatusPages")

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception<InvalidParameterException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, ApiError.invalidParameter(cause.message ?: "Invalid parameter"))
        }
        exception<Throwable> { call, cause ->
            logger.error("Unhandled error", cause)
            call.respond(HttpStatusCode.InternalServerError, ApiError.internal())
        }
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(HttpStatusCode.NotFound, ApiError.notFound("Route not found: ${call.request.local.uri}"))
        }
    }
}