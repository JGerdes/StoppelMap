package com.jonasgerdes.stoppelmap.news.data.source.local

import androidx.room.Transaction
import com.jonasgerdes.stoppelmap.news.data.entity.Article
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

private typealias ArticleListener = () -> Any

class LocalNewsSource(
    private val database: NewsDatabase
) {

    private val newsListeners = mutableListOf<ArticleListener>()

    @Transaction
    suspend fun persist(articles: List<Article>) {

        database.articleDao().insert(articles.map { article ->
            article.asDatabaseArticle()
        })

        articles.forEach { article ->
            database.imageDao().insert(article.images.asDatabaseImages(article.url))
        }
        newsListeners.forEach { it() }
    }

    suspend fun getNewsStream(): ReceiveChannel<List<Article>> = Channel<List<Article>>(Channel.CONFLATED).apply {
        send(database.articleDao().getAllArticlesWithImages().asArticles())
        val listener = { offer(database.articleDao().getAllArticlesWithImages().asArticles()) }
        newsListeners.add(listener)

        invokeOnClose {
            newsListeners.remove(listener)
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