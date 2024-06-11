package com.jonasgerdes.stoppelmap.news.data.remote

import com.jonasgerdes.stoppelmap.shared.network.executeRequest
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class RemoteNewsSource(
    private val baseUrl: String,
    private val httpClient: HttpClient
) {

    suspend fun getFirstPage(): Response<NewsResponse> =
        httpClient.executeRequest<NewsResponse> {
            get {
                url(urlString = "$baseUrl/news")
                parameter("page-size", 10)
            }
        }

    suspend fun loadPage(pageUrl: String): Response<NewsResponse> =
        httpClient.executeRequest<NewsResponse> {
            httpClient.get {
                url(urlString = pageUrl)
            }
        }
}
