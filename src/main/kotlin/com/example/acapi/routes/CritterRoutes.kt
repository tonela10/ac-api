package com.example.acapi.routes

import com.example.acapi.model.ApiError
import com.example.acapi.model.CritterType
import com.example.acapi.model.Game
import com.example.acapi.model.Hemisphere
import com.example.acapi.model.InvalidParameterException
import com.example.acapi.repo.CritterRepository
import com.example.acapi.util.intOrNull
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

fun Route.critterRoutes(repo: CritterRepository) {
    route("/critters") {
        get {
            val params = call.request.queryParameters
            val type = params["type"]?.let { raw ->
                CritterType.entries.firstOrNull { it.name.equals(raw, ignoreCase = true) || raw.equals(it.serialName(), ignoreCase = true) }
                    ?: throw InvalidParameterException("type must be one of fish/bug/sea_creature, got '$raw'")
            }
            val game = params["game"]?.let { code ->
                Game.fromCode(code) ?: throw InvalidParameterException("Unknown game code '$code'")
            }
            val hemisphere = params["hemisphere"]?.let { raw ->
                when (raw.lowercase()) {
                    "northern" -> Hemisphere.NORTHERN
                    "southern" -> Hemisphere.SOUTHERN
                    else -> throw InvalidParameterException("hemisphere must be northern or southern, got '$raw'")
                }
            }
            val month = params.intOrNull("month")?.also {
                if (it !in 1..12) throw InvalidParameterException("month must be 1-12, got $it")
            }

            val filtered = repo.list(
                type = type,
                game = game,
                location = params.stringOrNull("location"),
                month = month,
                hemisphere = hemisphere,
                timeOfDay = params.stringOrNull("time_of_day"),
                minPrice = params.intOrNull("min_price"),
                maxPrice = params.intOrNull("max_price"),
                shadowSize = params.stringOrNull("shadow_size"),
                rarity = params.stringOrNull("rarity"),
            )

            val response = paginate(
                all = filtered,
                page = params.pageParam(),
                pageSize = params.pageSizeParam(),
                basePath = "/api/v1/critters",
                queryString = params.rebuildQueryString(),
            )
            call.respond(response)
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ApiError.invalidParameter("Missing id"),
            )
            val critter = repo.findById(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                ApiError.notFound("Critter with id '$id' not found"),
            )
            call.respond(critter)
        }
    }
}

private fun CritterType.serialName(): String = when (this) {
    CritterType.FISH -> "fish"
    CritterType.BUG -> "bug"
    CritterType.SEA_CREATURE -> "sea_creature"
}