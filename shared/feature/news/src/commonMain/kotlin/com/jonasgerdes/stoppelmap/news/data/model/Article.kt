package com.jonasgerdes.stoppelmap.news.data.model

import kotlinx.datetime.LocalDate
import kotlin.jvm.JvmInline

data class Article(
    val slug: ArticleSlug,
    val title: String,
    val teaser: String,
    val publishDate: LocalDate,
    val url: String,
    val images: List<Image>,
    val isUnread: Boolean = true,
)

@JvmInline
value class ArticleSlug(val slug: String)
