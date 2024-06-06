package com.jonasgerdes.stoppelmap.server.crawler.model

import kotlinx.datetime.LocalDate

data class ScrapedArticle(
    val slug: String,
    val title: String,
    val content: String,
    val images: List<ScrapedImage>,
    val description: String,
    val publishDate: LocalDate,
)