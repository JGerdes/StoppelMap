package com.jonasgerdes.stoppelmap.server.news.model

import kotlinx.datetime.LocalDate

data class Article(
    val slug: String,
    val title: String,
    val content: String,
    val images: List<Image>,
    val description: String,
    val publishDate: LocalDate,
)