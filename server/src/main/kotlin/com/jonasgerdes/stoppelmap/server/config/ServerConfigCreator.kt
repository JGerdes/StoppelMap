package com.jonasgerdes.stoppelmap.server.config

import io.ktor.server.config.ApplicationConfig

fun ApplicationConfig.toServerConfig(
    host: String,
) = ServerConfig(
    environment = property("stoppelmap-server.environment").getString().let {
        try {
            ServerConfig.Environment.valueOf(it.uppercase())
        } catch (iae: IllegalArgumentException) {
            ServerConfig.Environment.PRD
        }
    },
    version = property("stoppelmap-server.version").getString(),
    externalDomain = propertyOrNull("stoppelmap-server.external-domain")
        ?.getString() ?: "http://$host",
    sqliteDirectory = property("stoppelmap-server.sqlite-dir").getString(),
    staticDirectory = property("stoppelmap-server.static-dir").getString(),
    crawler = ServerConfig.Crawler(
        baseUrl = property("stoppelmap-server.crawler.base-url").getString(),
        slowMode = property("stoppelmap-server.crawler.slow-mode").getString().toBoolean(),
        imageCacheDir = property("stoppelmap-server.crawler.image-cache-dir").getString(),
        doInitialFullCrawl = property("stoppelmap-server.crawler.do-initial-full-crawl").getString()
            .toBoolean(),
        periodicCrawlHours = property("stoppelmap-server.crawler.periodic-crawl-hours")
            .getString()
            .split(',')
            .mapNotNull {
                try {
                    it.trim().toInt()
                } catch (nfe: NumberFormatException) {
                    null
                }
            }
            .toSet(),
    ),
    apiKey = property("stoppelmap-server.api-key").getString().also {
        if (it.isBlank()) throw IllegalArgumentException("Please provide an API key as environment variable `API_KEY`.")
    }
)