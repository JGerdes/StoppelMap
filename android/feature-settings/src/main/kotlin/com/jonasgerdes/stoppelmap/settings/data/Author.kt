package com.jonasgerdes.stoppelmap.settings.data

data class Author(
    val name: String,
    val work: String,
    val website: String? = null,
    val githubUrl: String? = null,
    val gitlabUrl: String? = null,
    val mail: String? = null
)
