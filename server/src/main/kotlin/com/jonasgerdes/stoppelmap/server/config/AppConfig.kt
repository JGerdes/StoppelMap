package com.jonasgerdes.stoppelmap.server.config

data class AppConfig(
    val environment: Environment,
    val stoppelmarktWebsiteBaseUrl: String,
    val version: String,
) {
    enum class Environment {
        DEV,
        PRD;

        val isDev get() = this == DEV
        val isPrd get() = this == PRD

    }
}
