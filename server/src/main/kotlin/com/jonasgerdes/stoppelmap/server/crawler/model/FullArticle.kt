package com.jonasgerdes.stoppelmap.server.crawler.model

import kotlinx.datetime.LocalDate

data class FullArticle(
    val slug: String,
    val title: String,
    val content: String,
    val images: List<Image>,
    val description: String,
    val publishDate: LocalDate,
)