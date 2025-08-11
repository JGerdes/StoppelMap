package com.jonasgerdes.stoppelmap.server.news

import com.jonasgerdes.stoppelmap.server.callextensions.assertHasApiKey
import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import com.jonasgerdes.stoppelmap.server.util.Response
import io.ktor.http.Parameters
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.MissingRequestParameterException
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Route.newsRoutes() {

    val newsController by inject<NewsController>()
    val config by inject<ServerConfig>()

    get("/news") {
        call.assertHasApiKey(config.apiKey)
        when (val result = newsController.getArticles(
            before = call.parameters.get("before")?.assertNotBlank("before"),
            pageSize = call.parameters.getInt(
                name = "page-size",
                defaultValue = 10,
                minimumValue = 1,
                maximumValue = 30
            ),
        )) {
            is Response.Error -> call.respond(result.code)
            is Response.Success -> call.respond(result.code, result.data)
        }
    }
}

private fun Parameters.getInt(
    name: String,
    defaultValue: Int? = null,
    minimumValue: Int? = null,
    maximumValue: Int? = null,
): Int {
    val parameter =
        get(name)?.toIntOrNull() ?: defaultValue ?: throw MissingRequestParameterException(name)
    return parameter.assertIn(name, minimumValue = minimumValue, maximumValue = maximumValue)
}

private fun Int.assertIn(name: String, minimumValue: Int? = null, maximumValue: Int? = null): Int {
    minimumValue?.let { if (this < it) throw BadRequestException("Request parameter $name must be at least $minimumValue") }
    maximumValue?.let { if (this > it) throw BadRequestException("Request parameter $name must be at most $maximumValue") }
    return this
}

private fun String.assertNotBlank(name: String) =
    ifBlank { throw BadRequestException("Request parameter $name can't be blank") }