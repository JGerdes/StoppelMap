package com.jonasgerdes.stoppelmap.server.config

data class AppConfig(
    val environment: Environment,
    val version: String,
    val crawler: Crawler,
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
    )
}
