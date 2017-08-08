package com.jonasgerdes.stoppelmap.model.versioning

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 08.08.17
 */
data class Message(
        val slug: String,
        val versions: List<Int>,
        val showAlways: Boolean,
        val title: String,
        val message: String
)