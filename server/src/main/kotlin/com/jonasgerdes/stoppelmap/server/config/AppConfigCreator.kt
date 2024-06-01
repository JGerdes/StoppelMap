package com.jonasgerdes.stoppelmap.server.config

import io.ktor.server.config.ApplicationConfig

fun ApplicationConfig.toAppConfig() = AppConfig(
    environment = property("ktor.environment").getString().let {
        try {
            AppConfig.Environment.valueOf(it.uppercase())
        } catch (iae: IllegalArgumentException) {
            AppConfig.Environment.PRD
        }
    }
)