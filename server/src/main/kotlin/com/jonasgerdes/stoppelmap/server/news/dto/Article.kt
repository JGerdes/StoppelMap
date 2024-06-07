package com.jonasgerdes.stoppelmap.server.news.dto

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val url: String,
    val title: String,
    val teaser: String,
    val publishDate: LocalDate,
    val images: List<Image>,
)