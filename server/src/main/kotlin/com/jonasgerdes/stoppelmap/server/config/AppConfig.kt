package com.jonasgerdes.stoppelmap.server.config

data class AppConfig(
    val environment: Environment
) {
    enum class Environment {
        DEV,
        PRD;

        val isDev get() = this == DEV
        val isPrd get() = this == PRD

    }
}
