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
    sqliteDirectory = property("stoppelmap-server.sqlite-dir").getString(),
    staticDirectory = property("stoppelmap-server.static-dir").getString(),
    websiteDirectory = property("stoppelmap-server.website-dir").getString(),
    templatesDirectory = property("stoppelmap-server.templates-dir").getString(),
    deeplinkConfigFile = property("stoppelmap-server.deeplink-config-file").getString(),
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
    appConfigFile = property("stoppelmap-server.app-config-file").getString(),
    apiDomain = propertyOrNull("stoppelmap-server.api-domain")?.getString()?.removePrefix("https://") ?: host,
    deeplinkDomain = propertyOrNull("stoppelmap-server.deeplink-domain")?.getString()?.removePrefix("https://") ?: host,
    apiKey = property("stoppelmap-server.api-key").getString().also {
        if (it.isBlank()) throw IllegalArgumentException("Please provide an API key as environment variable `API_KEY`.")
    },
    metricCredentials = property("stoppelmap-server.metric-credentials").getString().also {
        if (it.isBlank()) throw IllegalArgumentException("Please provide credentials for metrics as environment variable `METRIC_CREDENTIALS`.")
    }
)