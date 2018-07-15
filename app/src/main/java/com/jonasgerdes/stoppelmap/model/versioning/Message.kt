package com.jonasgerdes.stoppelmap.model.versioning

data class Message(
        val slug: String,
        val versions: List<Int>,
        val showAlways: Boolean,
        val title: String,
        val message: String
)