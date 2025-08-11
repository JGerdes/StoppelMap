package com.jonasgerdes.stoppelmap.server.config

data class ServerConfig(
    val environment: Environment,
    val version: String,
    val sqliteDirectory: String,
    val staticDirectory: String,
    val templatesDirectory: String,
    val deeplinkConfigFile: String,
    val websiteDirectory: String,
    val crawler: Crawler,
    val appConfigFile: String,
    val apiKey: String,
    val apiDomain: String,
    val deeplinkDomain: String,
    val metricCredentials: String,
) {
    enum class Environment {
        DEV,
        PRD;

        val isDev get() = this == DEV
        val isPrd get() = this == PRD

    }

    data class Crawler(
        val baseUrl: String,
        val slowMode: Boolean,
        val imageCacheDir: String,
        val doInitialFullCrawl: Boolean,
        val periodicCrawlHours: Set<Int>,
    )
}
