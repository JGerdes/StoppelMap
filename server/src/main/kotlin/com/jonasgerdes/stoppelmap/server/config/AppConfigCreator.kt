package com.jonasgerdes.stoppelmap.server.config

import io.ktor.server.config.ApplicationConfig

fun ApplicationConfig.toAppConfig(
    host: String,
) = AppConfig(
    environment = property("stoppelmap-server.environment").getString().let {
        try {
            AppConfig.Environment.valueOf(it.uppercase())
        } catch (iae: IllegalArgumentException) {
            AppConfig.Environment.PRD
        }
    },
    version = property("stoppelmap-server.version").getString(),
    externalDomain = propertyOrNull("stoppelmap-server.external-domain")
        ?.getString() ?: "http://$host",
    sqliteDirectory = property("stoppelmap-server.sqlite-dir").getString(),
    crawler = AppConfig.Crawler(
        baseUrl = property("stoppelmap-server.crawler.base-url").getString(),
        slowMode = property("stoppelmap-server.crawler.slow-mode").getString().toBoolean(),
        imageCacheDir = property("stoppelmap-server.crawler.image-cache-dir").getString(),
    ),
)