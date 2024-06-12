@file:OptIn(ExperimentalCoroutinesApi::class)

package com.jonasgerdes.stoppelmap.news.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.jonasgerdes.stoppelmap.news.data.local.database.LocalNewsSource
import com.jonasgerdes.stoppelmap.news.data.model.Article
import com.jonasgerdes.stoppelmap.news.data.model.ArticleSortKey
import com.jonasgerdes.stoppelmap.news.data.model.Image
import com.jonasgerdes.stoppelmap.news.data.remote.NewsResponse
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import com.jonasgerdes.stoppelmap.news.data.remote.Article as RemoteArticle
import com.jonasgerdes.stoppelmap.news.data.remote.Image as RemoteImage

class NewsRepository(
    private val localNewsSource: LocalNewsSource,
    private val remoteNewsSource: RemoteNewsSource,
    private val userDataStore: DataStore<Preferences>,
) {
    private val lastSeenArticleKey = stringPreferencesKey("lastSeenArticle")
    fun getArticles(): Flow<List<Article>> = localNewsSource.getNews()

    private fun getLastSeenArticle() =
        userDataStore.data.map { it[lastSeenArticleKey]?.let(::ArticleSortKey) }

    fun getUnreadArticleCount(): Flow<Long> =
        getLastSeenArticle().flatMapLatest { lastSeenArticle ->
            if (lastSeenArticle == null) flowOf(0)
            else localNewsSource.getUnreadArticleCount(lastSeenArticle)
        }

    suspend fun loadLatestArticles() {
        processNewsResponse(
            remoteNewsSource.getNews(),
        )
    }

    suspend fun loadMoreArticles() {
        processNewsResponse(
            remoteNewsSource.getNews(localNewsSource.getOldestKey()?.value)
        )
    }

    suspend fun forceRefresh() {
        processNewsResponse(
            remoteNewsSource.getNews(),
            clearCache = true
        )
    }

    private suspend fun processNewsResponse(
        response: Response<NewsResponse>,
        clearCache: Boolean = false
    ) {
        when (response) {
            is Response.Error -> {
                // Todo: Do something
            }

            is Response.Success -> {
                val newArticles = response.body.articles.map { it.toArticle() }
                localNewsSource.upsertNews(newArticles, deleteExisting = clearCache)
            }
        }
    }

    suspend fun markAllArticlesRead() {
        val latestKey = localNewsSource.getLatestKey()?.value
        if (latestKey != null) {
            userDataStore.edit {
                it[lastSeenArticleKey] = latestKey
            }
        }
    }

}

private fun RemoteArticle.toArticle() =
    Article(
        sortKey = ArticleSortKey(sortKey),
        url = url,
        title = title,
        teaser = teaser,
        publishDate = publishDate,
        images = images.map { it.toImage() }
    )

private fun RemoteImage.toImage() =
    Image(
        uuid = uuid,
        url = url,
        copyright = copyright,
        caption = caption,
        blurHash = blurHash,
    )
