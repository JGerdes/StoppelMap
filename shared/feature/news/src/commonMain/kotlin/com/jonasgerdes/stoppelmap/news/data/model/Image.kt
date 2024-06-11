package com.jonasgerdes.stoppelmap.news.data.model

data class Image(
    val uuid: String,
    val url: String,
    val copyright: String?,
    val caption: String?,
    val blurHash: String,
)
