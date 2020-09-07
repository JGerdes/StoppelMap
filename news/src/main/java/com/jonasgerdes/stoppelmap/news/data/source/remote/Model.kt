package com.jonasgerdes.stoppelmap.news.data.source.remote

import com.squareup.moshi.JsonClass
import org.threeten.bp.LocalDate

@JsonClass(generateAdapter = true)
data class NewsResponse(
    val version: Int,
    val versionName: String,
    val articles: List<Article>,
    val pagination: Pagination
)

@JsonClass(generateAdapter = true)
data class Pagination(
    val previous: String?,
    val next: String?
)

@JsonClass(generateAdapter = true)
data class Article(
    val url: String,
    val title: String,
    val teaser: String?,
    val publishDate: LocalDate,
    val content: String?,
    val images: List<Image>
)

@JsonClass(generateAdapter = true)
data class Image(
    val url: String,
    val author: String?,
    val caption: String?
)

@JsonClass(generateAdapter = true)
data class Error(
    val code: Int,
    val message: String,
    val path: String
)

