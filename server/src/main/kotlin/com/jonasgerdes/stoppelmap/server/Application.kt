package com.jonasgerdes.stoppelmap.server

import com.jonasgerdes.stoppelmap.server.config.toAppConfig
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.plugins.callloging.CallLogging
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.koin.core.logger.Level
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.ktorModule() {
    val appConfig = environment.config.toAppConfig()
    environment.log.info("Starting with environment ${appConfig.environment}")
    install(CallLogging)
    install(Koin) {
        printLogger(if (appConfig.environment.isDev) Level.DEBUG else Level.INFO)
        modules(
            module {
                single { appConfig }
                single { environment.log }
            },
        )
    }

    routing {
        get("/") {
            call.respondText("Hello Stoppelmarkt!")
        }
    }
}
