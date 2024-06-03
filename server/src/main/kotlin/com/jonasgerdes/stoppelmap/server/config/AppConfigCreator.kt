package com.jonasgerdes.stoppelmap.server.config

import io.ktor.server.config.ApplicationConfig

fun ApplicationConfig.toAppConfig() = AppConfig(
    environment = property("stoppelmap-server.environment").getString().let {
        try {
            AppConfig.Environment.valueOf(it.uppercase())
        } catch (iae: IllegalArgumentException) {
            AppConfig.Environment.PRD
        }
    },
    version = property("stoppelmap-server.version").getString(),
    crawler = AppConfig.Crawler(
        baseUrl = property("stoppelmap-server.crawler.base-url").getString(),
        slowMode = property("stoppelmap-server.crawler.slow-mode").getString().toBoolean()
    ),
)