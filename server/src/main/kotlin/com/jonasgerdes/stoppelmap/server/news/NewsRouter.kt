package com.jonasgerdes.stoppelmap.server.news

import io.ktor.http.HttpStatusCode
import io.ktor.http.Parameters
import io.ktor.server.application.call
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.MissingRequestParameterException
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject

fun Routing.newsRoutes() {

    val newsController by inject<NewsController>()

    get("/news") {
        when (val result = newsController.getArticles(
            page = call.parameters.getInt(name = "page", defaultValue = 0, minimumValue = 0),
            pageSize = call.parameters.getInt(
                name = "page-size",
                defaultValue = 10,
                minimumValue = 1,
                maximumValue = 30
            ),
        )) {
            is NewsController.Response.Error -> call.respond(result.code)
            is NewsController.Response.Success -> call.respond(HttpStatusCode.OK, result.data)
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