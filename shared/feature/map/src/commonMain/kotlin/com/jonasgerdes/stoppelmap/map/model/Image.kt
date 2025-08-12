package com.jonasgerdes.stoppelmap.map.model

data class Image(
    val url: String,
    val blurHash: String,
    val caption: String? = null
)
