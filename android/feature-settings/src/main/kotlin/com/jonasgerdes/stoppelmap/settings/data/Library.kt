package com.jonasgerdes.stoppelmap.settings.data

data class Library(
    val name: String,
    val author: String,
    val license: License,
    val gitlabUrl: String? = null,
    val githubUrl: String? = null,
    val sourceUrl: String? = null
)


data class Libraries(
    val libraries: List<Library>
)
