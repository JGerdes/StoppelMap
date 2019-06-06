package com.jonasgerdes.stoppelmap.news.data.source.local

import androidx.room.InvalidationTracker.Observer
import androidx.room.Transaction
import com.jonasgerdes.stoppelmap.news.data.entity.Article
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

class LocalNewsSource(
    private val database: NewsDatabase
) {
    @Transaction
    suspend fun persist(articles: List<Article>) {

        database.articleDao().insert(articles.map { article ->
            article.asDatabaseArticle()
        })

        articles.forEach { article ->
            database.imageDao().insert(article.images.asDatabaseImages(article.url))
        }
    }

    suspend fun getNewsStream(): ReceiveChannel<List<Article>> = Channel<List<Article>>(Channel.CONFLATED).apply {
        send(database.articleDao().getAllArticlesWithImages().asArticles())
        val databaseInvalidationObserver = object : Observer("articles", "images") {
            override fun onInvalidated(tables: MutableSet<String>) {
                offer(database.articleDao().getAllArticlesWithImages().asArticles())
            }

        }
        database.invalidationTracker.addObserver(databaseInvalidationObserver)

        invokeOnClose {
            database.invalidationTracker.removeObserver(databaseInvalidationObserver)
        }
    }

    suspend fun clear() {
        database.articleDao().clearAll()
    }

    @Transaction
    suspend fun clearAndThenPersist(articles: List<Article>) {
        clear()
        persist(articles)
    }

}