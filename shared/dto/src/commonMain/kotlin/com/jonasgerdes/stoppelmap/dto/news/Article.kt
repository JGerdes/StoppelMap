package com.jonasgerdes.stoppelmap.dto.news

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class Article(
    val sortKey: String,
    val url: String,
    val title: String,
    val teaser: String,
    val publishDate: LocalDate,
    val images: List<Image>,
)