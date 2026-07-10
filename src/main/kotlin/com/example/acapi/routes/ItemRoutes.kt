package com.example.acapi.routes

import com.example.acapi.model.ApiError
import com.example.acapi.model.Game
import com.example.acapi.model.InvalidParameterException
import com.example.acapi.model.ItemCategory
import com.example.acapi.repo.ItemRepository
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

fun Route.itemRoutes(repo: ItemRepository) {
    route("/items") {
        get {
            val params = call.request.queryParameters
            val category = params["category"]?.let { raw ->
                ItemCategory.entries.firstOrNull { it.serialName().equals(raw, ignoreCase = true) }
                    ?: throw InvalidParameterException("Unknown category '$raw'")
            }
            val game = params["game"]?.let { code ->
                Game.fromCode(code) ?: throw InvalidParameterException("Unknown game code '$code'")
            }

            val filtered = repo.list(
                category = category,
                game = game,
                theme = params.stringOrNull("theme"),
                source = params.stringOrNull("source"),
                minPrice = params.intOrNull("min_price"),
                maxPrice = params.intOrNull("max_price"),
                size = params.stringOrNull("size"),
            )

            val response = paginate(
                all = filtered,
                page = params.pageParam(),
                pageSize = params.pageSizeParam(),
                basePath = "/api/v1/items",
                queryString = params.rebuildQueryString(),
            )
            call.respond(response)
        }

        get("{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(
                HttpStatusCode.BadRequest,
                ApiError.invalidParameter("Missing id"),
            )
            val item = repo.findById(id) ?: return@get call.respond(
                HttpStatusCode.NotFound,
                ApiError.notFound("Item with id '$id' not found"),
            )
            call.respond(item)
        }
    }
}

private fun ItemCategory.serialName(): String = when (this) {
    ItemCategory.FURNITURE -> "furniture"
    ItemCategory.CLOTHING -> "clothing"
    ItemCategory.TOOL -> "tool"
    ItemCategory.DIY_RECIPE -> "diy_recipe"
    ItemCategory.MATERIAL -> "material"
    ItemCategory.OTHER -> "other"
}