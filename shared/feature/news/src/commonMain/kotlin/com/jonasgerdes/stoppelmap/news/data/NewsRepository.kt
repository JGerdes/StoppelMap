package com.jonasgerdes.stoppelmap.news.data

import com.jonasgerdes.stoppelmap.news.data.local.database.LocalNewsSource
import com.jonasgerdes.stoppelmap.news.data.model.Article
import com.jonasgerdes.stoppelmap.news.data.model.ArticleSlug
import com.jonasgerdes.stoppelmap.news.data.model.Image
import com.jonasgerdes.stoppelmap.news.data.remote.NewsResponse
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import kotlinx.coroutines.flow.Flow
import com.jonasgerdes.stoppelmap.news.data.remote.Article as RemoteArticle
import com.jonasgerdes.stoppelmap.news.data.remote.Image as RemoteImage

class NewsRepository(
    private val localNewsSource: LocalNewsSource,
    private val remoteNewsSource: RemoteNewsSource,
) {

    private var nextPage: String? = null

    fun getArticles(): Flow<List<Article>> = localNewsSource.getNews()

    suspend fun loadMoreArticles() {
        val nextPage = nextPage
        processNewsResponse(
            if (nextPage == null) remoteNewsSource.getFirstPage()
            else remoteNewsSource.loadPage(nextPage)
        )
    }

    private suspend fun processNewsResponse(response: Response<NewsResponse>) {
        when (response) {
            is Response.Error -> {
                // Todo: Do something
            }

            is Response.Success -> {
                nextPage = response.body.pagination.next
                val newArticles = response.body.articles.map { it.toArticle() }
                localNewsSource.upsertNews(newArticles)
            }
        }
    }

}

private fun RemoteArticle.toArticle() =
    Article(
        slug = ArticleSlug(slug),
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
