package com.jonasgerdes.stoppelmap.server.news

import com.jonasgerdes.stoppelmap.dto.news.Article
import com.jonasgerdes.stoppelmap.dto.news.GetArticlesResponse
import com.jonasgerdes.stoppelmap.dto.news.Image
import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import com.jonasgerdes.stoppelmap.server.news.data.ArticleRepository
import com.jonasgerdes.stoppelmap.server.news.data.ImageRepository
import com.jonasgerdes.stoppelmap.server.util.Response
import com.jonasgerdes.stoppelmap.server.util.ULID
import io.ktor.http.HttpStatusCode

class NewsController(
    val articleRepository: ArticleRepository,
    val imageRepository: ImageRepository,
    val config: ServerConfig,
) {
    fun getArticles(
        before: String?,
        pageSize: Int,
    ): Response<GetArticlesResponse> {
        if (before != null && !ULID.isValid(before)) return Response.Error(
            HttpStatusCode.BadRequest,
            "Parameter before must be a valid sortKey"
        )
        val articles = when {
            before != null -> articleRepository.getBefore(sortKey = before, pageSize = pageSize)
            else -> articleRepository.getFirst(pageSize = pageSize)
        }
        if (articles.isEmpty()) return Response.Success(
            HttpStatusCode.NoContent, GetArticlesResponse(
                emptyList()
            )
        )
        val images = imageRepository.getAllForArticles(articles.map { it.slug })
        return Response.Success(
            data = GetArticlesResponse(
                articles = articles.map { article ->
                    Article(
                        sortKey = article.sortKey,
                        url = "https://www.stoppelmarkt.de/aktuelles/detail/${article.slug}/",
                        title = article.title,
                        images = images.filter { it.articleSlug == article.slug }.map {
                            Image(
                                uuid = it.uuid,
                                url = "https://${config.apiDomain}/static/images/${it.processedFile}",
                                caption = it.caption,
                                copyright = it.copyright,
                                blurHash = it.blurHash,
                            )
                        },
                        teaser = article.description,
                        publishDate = article.publishedOn,
                    )
                }
            )
        )
    }
}