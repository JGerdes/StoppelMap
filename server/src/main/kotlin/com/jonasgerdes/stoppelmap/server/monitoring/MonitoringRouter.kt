package com.jonasgerdes.stoppelmap.server.monitoring

import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import io.ktor.http.HttpStatusCode
import io.ktor.http.auth.AuthScheme.Bearer
import io.ktor.server.application.call
import io.ktor.server.request.header
import io.ktor.server.request.userAgent
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.util.logging.Logger
import org.koin.ktor.ext.inject

fun Routing.monitoringRoutes() {
    val monitoring: Monitoring by inject()
    val serverConfig: ServerConfig by inject()
    val logger: Logger by inject()

    get("/metrics") {
        val authorization = call.request.header("Authorization")
        if (authorization != "$Bearer ${serverConfig.metricCredentials}") {
            logger.warn("Unauthenticated call to /metrics by ${call.request.userAgent()}")
            call.respond(HttpStatusCode.NotFound) // Let's hide that there even are metrics
        } else {
            call.respond(monitoring.provideMetrics())
        }
    }
}