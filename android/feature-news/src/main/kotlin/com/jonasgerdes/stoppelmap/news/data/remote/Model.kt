package com.jonasgerdes.stoppelmap.news.data.remote

import kotlinx.datetime.LocalDate

@kotlinx.serialization.Serializable
data class NewsResponse(
    val version: Int? = null,
    val versionName: String? = null,
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
    val images: List<Image>
)

@kotlinx.serialization.Serializable
data class Image(
    val url: String,
    val copyright: String? = null,
    val caption: String? = null,
    val blurHash: String,
)
