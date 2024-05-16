package com.jonasgerdes.stoppelmap.licenses.model

data class ImageSource(
    val work: String,
    val author: String,
    val sourceUrl: String,
    val license: License? = null,
    val website: String? = null,
    val resource: Int? = null
)
