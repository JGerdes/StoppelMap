package com.jonasgerdes.stoppelmap.server.data

import com.jonasgerdes.stoppelmap.server.news.Article
import com.jonasgerdes.stoppelmap.server.news.ArticleQueries

class ArticleRepository(
    private val articleQueries: ArticleQueries,
) {

    fun upsertAll(articles: List<Article>) {
        articleQueries.transaction {
            articles.forEach {
                articleQueries.upsert(
                    slug = it.slug,
                    title = it.title,
                    description = it.description,
                    publishedOn = it.publishedOn,
                    content = it.content,
                    isVisible = it.isVisible,
                    createdAt = it.createdAt,
                    modifiedAt = it.modifiedAt,
                )
            }
        }
    }
}