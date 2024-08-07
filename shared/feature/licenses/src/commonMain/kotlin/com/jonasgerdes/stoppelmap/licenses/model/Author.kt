package com.jonasgerdes.stoppelmap.licenses.model

data class Author(
    val name: String,
    val work: String,
    val website: String? = null,
    val githubUrl: String? = null,
    val gitlabUrl: String? = null,
    val mail: String? = null
)
