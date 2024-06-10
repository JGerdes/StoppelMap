package com.jonasgerdes.stoppelmap.server.news.dto

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val url: String,
    val caption: String? = null,
    val copyright: String? = null,
    val blurHash: String,
)
