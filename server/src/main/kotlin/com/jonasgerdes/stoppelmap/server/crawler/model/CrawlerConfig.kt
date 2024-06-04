package com.jonasgerdes.stoppelmap.server.crawler.model

data class CrawlerConfig(
    val baseUrl: String,
    val userAgent: String,
    val slowMode: Boolean,
)
