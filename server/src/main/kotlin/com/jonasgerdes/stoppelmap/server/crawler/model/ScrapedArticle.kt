package com.jonasgerdes.stoppelmap.server.crawler.model

import kotlinx.datetime.LocalDate

data class ScrapedArticle(
    val slug: String,
    val title: String,
    val content: String,
    val images: List<ScrapedImage>,
    val description: String,
    val publishDate: LocalDate,
) {

    fun toFullArticle(
        images: List<Image>,
    ) = FullArticle(
        slug = slug,
        title = title,
        content = content,
        images = images,
        description = description,
        publishDate = publishDate,

        )
}