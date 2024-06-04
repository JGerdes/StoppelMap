package com.jonasgerdes.stoppelmap.server.crawler.model

import kotlinx.datetime.LocalDate

data class ArticlePreview(
    val slug: String,
    val title: String,
    val description: String,
    val publishDate: LocalDate
)