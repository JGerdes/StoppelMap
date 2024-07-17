package com.jonasgerdes.stoppelmap.server.news.data

import com.jonasgerdes.stoppelmap.server.news.data.Image
import com.jonasgerdes.stoppelmap.server.news.data.ImageQueries

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
                    originalUrl = it.originalUrl,
                    originalFile = it.originalFile,
                    processedFile = it.processedFile,
                )
            }
        }
    }

    fun getAllForArticles(articleSlugs: List<String>) =
        imageQueries.getAllForArticles(articleSlugs).executeAsList()
}