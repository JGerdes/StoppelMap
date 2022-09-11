package com.jonasgerdes.stoppelmap.news.data.remote

import kotlinx.datetime.LocalDate

@kotlinx.serialization.Serializable
data class NewsResponse(
    val version: Int,
    val versionName: String,
    val articles: List<Article>,
    val pagination: Pagination
)

@kotlinx.serialization.Serializable
data class Pagination(
    val previous: String? = null,
    val next: String? = null,
)

@kotlinx.serialization.Serializable
data class Article(
    val url: String,
    val title: String,
    val teaser: String? = null,
    val publishDate: LocalDate,
    val content: String? = null,
    val images: List<Image>
)

@kotlinx.serialization.Serializable
data class Image(
    val url: String,
    val author: String? = null,
    val caption: String?
)
