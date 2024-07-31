package com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote

import com.jonasgerdes.stoppelmap.dto.config.RemoteAppConfig
import com.jonasgerdes.stoppelmap.shared.network.apiKeyHeader
import com.jonasgerdes.stoppelmap.shared.network.executeRequest
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType

class RemoteAppConfigSource(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val apiKey: String,
) {

    suspend fun getRemoteAppConfig(): Response<RemoteAppConfig> =
        httpClient.executeRequest<RemoteAppConfig> {
            get("$baseUrl/static/app-config.json") {
                apiKeyHeader(apiKey)
                header("api-key", apiKey)
                accept(ContentType.Application.Json)
            }
        }

}
