package com.jonasgerdes.stoppelmap.model.entity

import java.util.*

data class JsonEventWrapper(val events: List<JsonEvent>)
data class JsonEvent(
        val uuid: String,
        val name: String,
        val start: String,
        val end: String?,
        val locationUuid: String? = null,
        val description: String? = null,
        val facebookUrl: String? = null,
        val artists: List<String>? = emptyList(),
        val tags: List<String>? = emptyList()
)
