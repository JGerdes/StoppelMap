package com.jonasgerdes.stoppelmap.dto.news

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val uuid: String,
    val url: String,
    val caption: String? = null,
    val copyright: String? = null,
    val blurHash: String,
)
