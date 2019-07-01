package com.jonasgerdes.stoppelmap.news.data.entity

import org.threeten.bp.LocalDate

data class Article(
    val url: String,
    val title: String,
    val teaser: String,
    val publishDate: LocalDate,
    val content: String,
    val images: List<Image>
)