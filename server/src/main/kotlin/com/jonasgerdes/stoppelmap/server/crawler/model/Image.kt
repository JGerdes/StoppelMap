package com.jonasgerdes.stoppelmap.server.crawler.model

import java.io.File

data class Image(
    val url: String,
    val caption: String? = null,
    val author: String? = null,
    val localFile: File,
    val blurHash: String,
)

data class ScrapedImage(
    val url: String,
    val caption: String? = null,
    val author: String? = null,
)