package com.jonasgerdes.stoppelmap.server.appconfig

import com.jonasgerdes.stoppelmap.server.callextensions.assertHasApiKey
import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import java.io.File

fun Route.appConfigRoutes() {

    val config by inject<ServerConfig>()

    get("/app-config") {
        call.assertHasApiKey(config.apiKey)
        call.respondFile(File(config.appConfigFile))
    }
}