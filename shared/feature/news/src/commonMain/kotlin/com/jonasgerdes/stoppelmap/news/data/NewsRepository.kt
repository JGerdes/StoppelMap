package com.jonasgerdes.stoppelmap.news.data

import com.jonasgerdes.stoppelmap.news.data.model.Article
import com.jonasgerdes.stoppelmap.news.data.model.Image
import com.jonasgerdes.stoppelmap.news.data.remote.NewsResponse
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.jonasgerdes.stoppelmap.news.data.remote.Article as RemoteArticle
import com.jonasgerdes.stoppelmap.news.data.remote.Image as RemoteImage

class NewsRepository(
    private val remoteNewsSource: RemoteNewsSource,
) {

    private var nextPage: String? = null
    private val articles = MutableStateFlow(emptyList<Article>())


    fun getArticles(): Flow<List<Article>> = articles.asStateFlow()

    suspend fun loadMoreArticles() {
        val nextPage = nextPage
        processNewsResponse(
            if (nextPage == null) remoteNewsSource.getFirstPage()
            else remoteNewsSource.loadPage(nextPage)
        )
    }

    private fun processNewsResponse(response: Response<NewsResponse>) {
        when (response) {
            is Response.Error -> {
                // Todo: Do something
            }

            is Response.Success -> {
                nextPage = response.body.pagination.next
                val newArticles = response.body.articles.map { it.toArticle() }
                articles.value =
                    articles.value.union(newArticles).sortedByDescending { it.publishDate }
            }
        }
    }

}

private fun RemoteArticle.toArticle() =
    Article(
        url = url,
        title = title,
        teaser = teaser,
        publishDate = publishDate,
        images = images.map { it.toImage() }
    )

private fun RemoteImage.toImage() =
    Image(
        url = url,
        copyright = copyright,
        caption = caption,
        blurHash = blurHash,
    )
