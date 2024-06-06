package com.jonasgerdes.stoppelmap.server.news.model

data class Image(
    val uuid: String,
    val caption: String? = null,
    val author: String? = null,
    val blurHash: String,
)
