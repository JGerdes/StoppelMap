package com.jonasgerdes.stoppelmap.server.util

private const val userAgentRegexString =
    "^StoppelMap (.*?)(-debug|-release|) (Android|iOS)\\(([\\d.,]{1,4}) on (.*)\\/(.*)\\)"
private val userAgentRegex = userAgentRegexString.toRegex()

fun String.parseUserAgent(): UserAgent? = userAgentRegex.find(this)?.groupValues
    ?.let {
        UserAgent(
            appVersion = it[1],
            buildType = it[2].removePrefix("-").ifBlank { "release" },
            os = it[3] + " " + it[4],
            manufacturer = it[5],
            device = it[6],
        )
    }


data class UserAgent(
    val appVersion: String,
    val buildType: String,
    val os: String,
    val manufacturer: String,
    val device: String,
)