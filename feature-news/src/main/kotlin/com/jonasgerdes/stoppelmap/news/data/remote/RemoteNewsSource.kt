package com.jonasgerdes.stoppelmap.news.data.remote

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class RemoteNewsSource(
    private val httpClient: HttpClient
) {

    suspend fun getFirstPage(): NewsResponse =
        httpClient.get {
            url(urlString = "https://app.stoppelmap.de/news")
        }.body()
}
