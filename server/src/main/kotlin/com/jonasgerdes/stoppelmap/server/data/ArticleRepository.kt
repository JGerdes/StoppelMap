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

    fun getPage(page: Int, pageSize: Int): List<Article> {
        return articleQueries.getVisible(
            limit = pageSize.toLong(),
            offset = (page * pageSize).toLong()
        ).executeAsList()
    }
    
    fun getCount() = articleQueries.countVisible().executeAsOne()
}