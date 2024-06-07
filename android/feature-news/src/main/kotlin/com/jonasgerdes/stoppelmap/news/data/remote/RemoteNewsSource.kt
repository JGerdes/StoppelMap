package com.jonasgerdes.stoppelmap.news.data.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class RemoteNewsSource(
    private val httpClient: HttpClient
) {

    suspend fun getFirstPage(): NewsResponse =
        httpClient.get {
            url(urlString = "https://app.stoppelmap.de/news")
            parameter("page-size", 10)
        }.body()

    suspend fun loadPage(pageUrl: String): NewsResponse =
        httpClient.get {
            url(urlString = pageUrl)
        }.body()
}
