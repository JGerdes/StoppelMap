package com.jonasgerdes.stoppelmap.server.data

import com.jonasgerdes.stoppelmap.server.news.Article
import com.jonasgerdes.stoppelmap.server.news.ArticleQueries

class ArticleRepository(
    private val articleQueries: ArticleQueries
) {

    fun upsertAll(articles: List<Article>) {
        articleQueries.transaction {
            articles.forEach {
                articleQueries.insert(it)
            }
        }
    }
}