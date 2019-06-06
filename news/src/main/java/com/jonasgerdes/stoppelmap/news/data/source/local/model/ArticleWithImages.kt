package com.jonasgerdes.stoppelmap.news.data.source.local.model

import androidx.room.Embedded
import androidx.room.Relation


data class ArticleWithImages(
    @Embedded
    val article: Article,

    @Relation(parentColumn = "url", entityColumn = "article_url", entity = Image::class)
    val images: List<Image>
)