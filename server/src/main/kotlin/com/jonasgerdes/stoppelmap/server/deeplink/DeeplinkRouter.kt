package com.jonasgerdes.stoppelmap.server.deeplink

import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.server.http.content.staticFiles
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import java.io.File

fun Route.deeplinkRoutes() {

    val deeplinkController by inject<DeeplinkController>()
    val serverConfig by inject<ServerConfig>()

    staticFiles("/.well-known", File(serverConfig.websiteDirectory, ".well-known")) {
        enableAutoHeadResponse()
    }

    get("/{slug}") {
        val slug = call.parameters["slug"]?.trim()?.take(128)
        val page = deeplinkController.getDeeplinkPage(slug)
        if (page == null) {
            call.respondFile(File(serverConfig.templatesDirectory, "404.html"))
        } else {
            call.respondText(page, ContentType.Text.Html, HttpStatusCode.OK)
        }
    }
}