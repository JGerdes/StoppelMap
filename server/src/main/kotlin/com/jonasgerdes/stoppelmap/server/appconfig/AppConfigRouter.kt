package com.jonasgerdes.stoppelmap.server.appconfig

import com.jonasgerdes.stoppelmap.server.callextensions.assertHasApiKey
import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import io.ktor.server.application.call
import io.ktor.server.response.respondFile
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import org.koin.ktor.ext.inject
import java.io.File

fun Routing.appConfigRoutes() {

    val config by inject<ServerConfig>()

    get("/app-config") {
        call.assertHasApiKey(config.apiKey)
        call.respondFile(File(config.appConfigFile))
    }
}