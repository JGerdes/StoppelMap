package com.jonasgerdes.stoppelmap.server.data

import com.jonasgerdes.stoppelmap.server.news.Image
import com.jonasgerdes.stoppelmap.server.news.ImageQueries

class ImageRepository(
    private val imageQueries: ImageQueries,
) {
    fun upsertAll(images: List<Image>) {
        imageQueries.transaction {
            images.forEach {
                imageQueries.upsert(
                    uuid = it.uuid,
                    articleSlug = it.articleSlug,
                    caption = it.caption,
                    copyright = it.copyright,
                    blurHash = it.blurHash,
                    originalUrl = it.originalUrl
                )
            }
        }
    }

    fun getAllForArticles(articleSlugs: List<String>) =
        imageQueries.getAllForArticles(articleSlugs).executeAsList()
}