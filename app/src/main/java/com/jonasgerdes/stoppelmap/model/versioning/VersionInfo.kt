package com.jonasgerdes.stoppelmap.model.versioning

data class VersionInfo(
        val version: HashMap<String, Release>,
        val messages: List<Message>?,
        var latest: Release? = null
)