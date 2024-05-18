package com.jonasgerdes.stoppelmap.licenses.model

data class Library(
    val name: String,
    val author: String,
    val license: License,
    val sourceUrl: String? = null
)
