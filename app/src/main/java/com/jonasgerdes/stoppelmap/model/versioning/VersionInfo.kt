package com.jonasgerdes.stoppelmap.model.versioning

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.08.17
 */
data class VersionInfo(
        val version: HashMap<String, Release>,
        val messages: List<Message>,
        var latest: Release? = null
)