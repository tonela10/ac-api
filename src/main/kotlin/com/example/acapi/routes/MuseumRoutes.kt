package com.example.acapi.routes

import com.example.acapi.model.ApiError
import com.example.acapi.model.Game
import com.example.acapi.model.InvalidParameterException
import com.example.acapi.model.MuseumCategory
import com.example.acapi.repo.MuseumRepository
import com.example.acapi.util.pageParam
import com.example.acapi.util.pageSizeParam
import com.example.acapi.util.paginate
import com.example.acapi.util.rebuildQueryString
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.museumRoutes(repo: MuseumRepository) {
    route("/museum") {
        get {
            val params = call.request.queryParameters
            val category = params["category"]?.let { raw ->
                when (raw.lowercase()) {
                    "fossil" -> MuseumCategory.FOSSIL
                    "art" -> MuseumCategory.ART
                    else -> throw InvalidParameterException("category must be fossil or art, got '$raw'")
                }
            }
            val game = params["game"]?.let { code ->
                Game.fromCode(code) ?: throw InvalidParameterException("Unknown game code '$code'")
            }

            val filtered = repo.list(category = category, game = game)
            val response = paginate(
                all = filtered,
                page = params.pageParam(),
                pageSize = params.pageSizeParam(),
                basePath = "/api/v1/museum",
                queryString = params.rebuildQueryString(),
            )
            call.respond(response)
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ApiError.invalidParameter("Missing id"),
            )
            val entry = repo.findById(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                ApiError.notFound("Museum entry with id '$id' not found"),
            )
            call.respond(entry)
        }
    }
}