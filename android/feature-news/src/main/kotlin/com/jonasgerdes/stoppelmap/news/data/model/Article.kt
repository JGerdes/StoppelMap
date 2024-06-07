package com.jonasgerdes.stoppelmap.news.data.model

import kotlinx.datetime.LocalDate

data class Article(
    val url: String,
    val title: String,
    val teaser: String?,
    val publishDate: LocalDate,
    val images: List<Image>
)
