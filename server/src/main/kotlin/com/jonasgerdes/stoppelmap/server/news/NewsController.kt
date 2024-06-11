package com.jonasgerdes.stoppelmap.server.news

import com.jonasgerdes.stoppelmap.server.config.AppConfig
import com.jonasgerdes.stoppelmap.server.data.ArticleRepository
import com.jonasgerdes.stoppelmap.server.data.ImageRepository
import com.jonasgerdes.stoppelmap.server.news.dto.Article
import com.jonasgerdes.stoppelmap.server.news.dto.GetArticlesResponse
import com.jonasgerdes.stoppelmap.server.news.dto.Image
import com.jonasgerdes.stoppelmap.server.news.dto.Pagination
import io.ktor.http.HttpStatusCode
import io.ktor.http.appendPathSegments
import io.ktor.http.takeFrom
import io.ktor.server.util.url

class NewsController(
    val articleRepository: ArticleRepository,
    val imageRepository: ImageRepository,
    val config: AppConfig,
) {

    fun getArticles(
        page: Int,
        pageSize: Int,
    ): Response<GetArticlesResponse> {
        val lastPage = articleRepository.getCount() / pageSize
        if (page > lastPage) return Response.Error(
            HttpStatusCode.BadRequest,
            "Last page is $lastPage"
        )
        val articles = articleRepository.getPage(page = page, pageSize = pageSize)
        if (articles.isEmpty()) return Response.Error(HttpStatusCode.NotFound)
        val images = imageRepository.getAllForArticles(articles.map { it.slug })
        return Response.Success(
            data = GetArticlesResponse(
                articles = articles.map { article ->
                    Article(
                        url = "https://www.stoppelmarkt.de/aktuelles/detail/${article.slug}/",
                        title = article.title,
                        images = images.filter { it.articleSlug == article.slug }.map {
                            Image(
                                url = "${config.externalDomain}/static/images/${it.processedFile}",
                                caption = it.caption,
                                copyright = it.copyright,
                                blurHash = it.blurHash,
                            )
                        },
                        teaser = article.description,
                        publishDate = article.publishedOn,
                    )
                },
                pagination = Pagination(
                    previous = if (page > 0) url {
                        takeFrom(config.externalDomain)
                        appendPathSegments("news")
                        parameters.append("page", (page - 1).toString())
                        parameters.append("page-size", pageSize.toString())
                    } else null,
                    next = if (page < lastPage) url {
                        takeFrom(config.externalDomain)
                        appendPathSegments("news")
                        parameters.append("page", (page + 1).toString())
                        parameters.append("page-size", pageSize.toString())
                    } else null
                )
            )
        )
    }


    sealed interface Response<T> {
        data class Success<T>(val data: T) : Response<T>
        data class Error<T>(
            val code: HttpStatusCode,
            val message: String? = null
        ) : Response<T>
    }
}