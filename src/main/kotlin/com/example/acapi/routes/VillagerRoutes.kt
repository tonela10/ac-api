package com.example.acapi.routes

import com.example.acapi.model.ApiError
import com.example.acapi.model.Game
import com.example.acapi.model.InvalidParameterException
import com.example.acapi.repo.VillagerRepository
import com.example.acapi.util.pageParam
import com.example.acapi.util.pageSizeParam
import com.example.acapi.util.paginate
import com.example.acapi.util.rebuildQueryString
import com.example.acapi.util.stringOrNull
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.route

fun Route.villagerRoutes(repo: VillagerRepository) {
    route("/villagers") {
        get {
            val params = call.request.queryParameters
            val game = params["game"]?.let { code ->
                Game.fromCode(code) ?: throw InvalidParameterException("Unknown game code '$code'")
            }
            val birthdayMonth = params["birthday_month"]?.toIntOrNull()?.also {
                if (it !in 1..12) throw InvalidParameterException("birthday_month must be 1-12, got $it")
            }

            val filtered = repo.list(
                species = params.stringOrNull("species"),
                personality = params.stringOrNull("personality"),
                gender = params.stringOrNull("gender"),
                game = game,
                birthdayMonth = birthdayMonth,
                hobby = params.stringOrNull("hobby"),
                zodiac = params.stringOrNull("zodiac"),
            )

            val response = paginate(
                all = filtered,
                page = params.pageParam(),
                pageSize = params.pageSizeParam(),
                basePath = "/api/v1/villagers",
                queryString = params.rebuildQueryString(),
            )
            call.respond(response)
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ApiError.invalidParameter("Missing id"),
            )
            val villager = repo.findById(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                ApiError.notFound("Villager with id '$id' not found"),
            )
            call.respond(villager)
        }
    }
}