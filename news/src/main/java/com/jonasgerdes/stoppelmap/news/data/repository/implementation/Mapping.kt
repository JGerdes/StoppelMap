package com.jonasgerdes.stoppelmap.news.data.repository.implementation

import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.jonasgerdes.stoppelmap.news.data.entity.Image

private typealias RemoteArticle = com.jonasgerdes.stoppelmap.news.data.source.remote.Article
private typealias RemoteImage = com.jonasgerdes.stoppelmap.news.data.source.remote.Image

internal fun RemoteImage.asImage() = Image(
    url = url,
    author = author,
    caption = caption
)

@JvmName("RemoteImageAsEnttiy")
internal fun List<RemoteImage>.asImages() = map { it.asImage() }

internal fun RemoteArticle.asArticle() = Article(
    url = url,
    title = title,
    teaser = teaser,
    publishDate = publishDate,
    content = content,
    images = images.asImages()
)

@JvmName("RemoteArticleAsEnttiy")
internal fun List<RemoteArticle>.asArticles() = map { it.asArticle() }