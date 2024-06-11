package com.jonasgerdes.stoppelmap.news.data.remote

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class NewsResponse(
    val articles: List<Article>,
    val pagination: Pagination
)

@Serializable
data class Pagination(
    val previous: String? = null,
    val next: String? = null,
)

@Serializable
data class Article(
    val slug: String,
    val url: String,
    val title: String,
    val teaser: String,
    val publishDate: LocalDate,
    val images: List<Image>
)

@Serializable
data class Image(
    val uuid: String,
    val url: String,
    val copyright: String? = null,
    val caption: String? = null,
    val blurHash: String,
)
