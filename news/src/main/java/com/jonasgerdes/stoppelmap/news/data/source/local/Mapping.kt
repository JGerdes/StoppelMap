package com.jonasgerdes.stoppelmap.news.data.source.local

import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.jonasgerdes.stoppelmap.news.data.entity.Image
import com.jonasgerdes.stoppelmap.news.data.source.local.model.ArticleWithImages

private typealias DatabaseArticle = com.jonasgerdes.stoppelmap.news.data.source.local.model.Article
private typealias DatabaseImage = com.jonasgerdes.stoppelmap.news.data.source.local.model.Image

internal fun DatabaseImage.asImage() = Image(
    url = url,
    author = author,
    caption = caption
)

@JvmName("DatabaseImageAsEnttiy")
internal fun List<DatabaseImage>.asImages() = map { it.asImage() }

internal fun DatabaseArticle.asArticle(images: List<Image>) = Article(
    url = url,
    title = title,
    teaser = teaser,
    publishDate = publishDate,
    content = content,
    images = images
)

@JvmName("DatabaseArticleWithImagesAsEntity")
internal fun List<ArticleWithImages>.asArticles() = map { it.asArticle() }

internal fun ArticleWithImages.asArticle() = Article(
    url = article.url,
    title = article.title,
    teaser = article.teaser,
    publishDate = article.publishDate,
    content = article.content,
    images = images.asImages()
)


internal fun Image.asDatabaseImage(articleUrl: String) = DatabaseImage(
    url = url,
    author = author,
    caption = caption,
    articleUrl = articleUrl
)

@JvmName("EntityAsDatabaseImage")
internal fun List<Image>.asDatabaseImages(articleUrl: String) = map { it.asDatabaseImage(articleUrl) }

internal fun Article.asDatabaseArticle() = DatabaseArticle(
    url = url,
    title = title,
    teaser = teaser,
    publishDate = publishDate,
    content = content
)

@JvmName("EntityAsDatabaseArticle")
internal fun List<Article>.asDatabaseArticle() = map { it.asDatabaseArticle() }