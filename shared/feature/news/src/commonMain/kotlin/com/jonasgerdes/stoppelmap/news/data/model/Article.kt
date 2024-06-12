package com.jonasgerdes.stoppelmap.news.data.model

import kotlinx.datetime.LocalDate
import kotlin.jvm.JvmInline

data class Article(
    val sortKey: ArticleSortKey,
    val title: String,
    val teaser: String,
    val publishDate: LocalDate,
    val url: String,
    val images: List<Image>,
    val isUnread: Boolean = true,
) {
    val sortKeyString get() = sortKey.value
}

@JvmInline
value class ArticleSortKey(val value: String)
