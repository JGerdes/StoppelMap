package com.jonasgerdes.stoppelmap.preperation.entity

data class JsonEventWrapper(val events: List<JsonEvent>)
data class JsonEvent(
    val uuid: String,
    val name: String,
    val start: String,
    val end: String?,
    val locationUuid: String? = null,
    val locationName: String? = null,
    val description: String? = null,
    val facebookUrl: String? = null,
    val artists: List<String>? = emptyList(),
    val tags: List<String>? = emptyList(),
    val isOfficial: Boolean = false
)
