package com.jonasgerdes.stoppelmap.news.data.source.remote

import org.threeten.bp.LocalDate

data class NewsResponse(
    val version: Int,
    val versionName: String,
    val articles: List<Article>,
    val pagination: Pagination
)

data class Pagination(
    val previous: String?,
    val next: String?
)

data class Article(
    val url: String,
    val title: String,
    val teaser: String,
    val publishDate: LocalDate,
    val content: String,
    val images: List<Image>
)

data class Image(
    val url: String,
    val author: String,
    val caption: String
)

data class Error(
    val code: Int,
    val message: String,
    val path: String
)

