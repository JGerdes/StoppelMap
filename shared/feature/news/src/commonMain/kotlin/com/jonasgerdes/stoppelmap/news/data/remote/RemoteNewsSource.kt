package com.jonasgerdes.stoppelmap.news.data.remote

import com.jonasgerdes.stoppelmap.shared.network.apiKeyHeader
import com.jonasgerdes.stoppelmap.shared.network.executeRequest
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class RemoteNewsSource(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val apiKey: String
) {

    suspend fun getNews(before: String? = null): Response<NewsResponse> =
        httpClient.executeRequest<NewsResponse> {
            get {
                url(urlString = "$baseUrl/news")
                before?.let { parameter("before", before) }
                parameter("page-size", 10)
                apiKeyHeader(apiKey)
            }
        }
}
