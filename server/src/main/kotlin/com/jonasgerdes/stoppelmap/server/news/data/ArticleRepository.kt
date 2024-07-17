package com.jonasgerdes.stoppelmap.server.news.data

import com.jonasgerdes.stoppelmap.server.news.data.Article
import com.jonasgerdes.stoppelmap.server.news.data.ArticleQueries

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
                    sortKey = it.sortKey,
                    createdAt = it.createdAt,
                    modifiedAt = it.modifiedAt,
                )
            }
        }
    }

    fun getFirst(pageSize: Int): List<Article> =
        articleQueries.getVisible(
            limit = pageSize.toLong()
        ).executeAsList()

    fun getBefore(sortKey: String, pageSize: Int): List<Article> =
        articleQueries.getVisibleBefore(
            before = sortKey,
            limit = pageSize.toLong()
        ).executeAsList()

    fun getCount() = articleQueries.countVisible().executeAsOne()
    fun getExistingPublishDates() = articleQueries
        .countExistingPublishDates { publishedOn, count -> publishedOn to count }
        .executeAsList()
        .toMap()
}