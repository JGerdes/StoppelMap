package com.jonasgerdes.stoppelmap.news.data.local.database

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.jonasgerdes.stoppelmap.news.data.model.Article
import com.jonasgerdes.stoppelmap.news.data.model.ArticleSortKey
import com.jonasgerdes.stoppelmap.news.data.model.Image
import com.jonasgerdes.stoppelmap.news.database.model.NewsDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext

class LocalNewsSource(
    private val newsDatabase: NewsDatabase,
) {

    fun getNews(): Flow<List<Article>> =
        combine(
            newsDatabase.articleQueries.getAll().asFlow().mapToList(Dispatchers.IO),
            newsDatabase.imageQueries.getAll().asFlow().mapToList(Dispatchers.IO),
        ) { articles, images ->
            articles.map { dbArticle ->
                Article(
                    sortKey = ArticleSortKey(dbArticle.sortKey),
                    title = dbArticle.title,
                    teaser = dbArticle.teaser,
                    publishDate = dbArticle.publishedOn,
                    url = dbArticle.url,
                    images = images.filter { it.articleKey == dbArticle.sortKey }
                        .map {
                            Image(
                                uuid = it.uuid,
                                url = it.url,
                                copyright = it.copyright,
                                caption = it.caption,
                                blurHash = it.blurHash,
                            )
                        },
                )
            }
        }

    suspend fun upsertNews(articles: List<Article>, deleteExisting: Boolean = false) {
        withContext(Dispatchers.IO) {
            newsDatabase.transaction {
                if (deleteExisting) {
                    newsDatabase.imageQueries.deleteAll()
                    newsDatabase.articleQueries.deleteAll()
                }
                articles.forEach { article ->
                    newsDatabase.articleQueries.upsert(
                        sortKey = article.sortKey.value,
                        title = article.title,
                        teaser = article.teaser,
                        url = article.url,
                        publishedOn = article.publishDate
                    )
                    newsDatabase.imageQueries.deleteAllForArticle(article.sortKey.value)
                    article.images.forEach { image ->
                        newsDatabase.imageQueries.upsert(
                            uuid = image.uuid,
                            articleKey = article.sortKey.value,
                            caption = image.caption,
                            copyright = image.copyright,
                            blurHash = image.blurHash,
                            url = image.url,
                        )
                    }
                }
            }
        }
    }

    fun getUnreadArticleCount(sortKey: ArticleSortKey): Flow<Long> =
        newsDatabase.articleQueries.countNewerThen(sortKey.value)
            .asFlow().mapToOne(Dispatchers.IO)


    suspend fun getOldestKey(): ArticleSortKey? =
        withContext(Dispatchers.IO) {
            newsDatabase.articleQueries.getOldestSortyKey().executeAsOneOrNull()
                ?.let(::ArticleSortKey)
        }

    suspend fun getLatestKey(): ArticleSortKey? =
        withContext(Dispatchers.IO) {
            newsDatabase.articleQueries.getLatestSortyKey().executeAsOneOrNull()
                ?.let(::ArticleSortKey)
        }
}