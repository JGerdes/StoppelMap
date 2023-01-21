package com.jonasgerdes.stoppelmap.about.data

data class ImageSource(
    val work: String,
    val author: String,
    val license: License? = null,
    val website: String? = null,
)
