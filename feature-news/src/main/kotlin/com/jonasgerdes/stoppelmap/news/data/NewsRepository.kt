package com.jonasgerdes.stoppelmap.news.data

import com.jonasgerdes.stoppelmap.news.data.model.Article
import com.jonasgerdes.stoppelmap.news.data.model.Image
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.jonasgerdes.stoppelmap.news.data.remote.Article as RemoteArticle
import com.jonasgerdes.stoppelmap.news.data.remote.Image as RemoteImage

class NewsRepository(
    private val remoteNewsSource: RemoteNewsSource,
) {

    fun getArticles(): Flow<List<Article>> = flow {
        val articles = remoteNewsSource.getFirstPage().articles.map { it.toArticle() }
        emit(articles)
    }

}

private fun RemoteArticle.toArticle() =
    Article(
        url = url,
        title = title,
        teaser = teaser,
        publishDate = publishDate,
        content = content,
        images = images.map { it.toImage() }
    )

private fun RemoteImage.toImage() =
    Image(
        url = url,
        author = author,
        caption = caption,
    )
