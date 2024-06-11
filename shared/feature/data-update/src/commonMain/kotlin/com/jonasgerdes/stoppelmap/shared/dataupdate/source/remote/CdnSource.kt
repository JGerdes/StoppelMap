package com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote

import com.jonasgerdes.stoppelmap.shared.dataupdate.io.readFully
import com.jonasgerdes.stoppelmap.shared.dataupdate.model.RemoteAppConfig
import com.jonasgerdes.stoppelmap.shared.network.executeRequest
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import com.jonasgerdes.stoppelmap.shared.network.runCatchingToResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import okio.FileSystem
import okio.Path
import okio.SYSTEM
import okio.use

class CdnSource(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val apiKey: String,
) {

    suspend fun getRemoteAppConfig(): Response<RemoteAppConfig> =
        httpClient.executeRequest<RemoteAppConfig> {
            get("$baseUrl/app-config.json") {
                apiKeyHeader()
                accept(ContentType.Application.Json)
            }
        }

    suspend fun downloadFile(
        destination: Path,
        name: String,
    ): Response<Path> {
        return httpClient.runCatchingToResponse<Path>(
            request = {
                get("$baseUrl/$name") {
                    apiKeyHeader()
                }
            },
            onSuccess = { response ->
                FileSystem.SYSTEM.sink(destination).use {
                    response.bodyAsChannel().readFully(it)
                }
                Response.Success(destination)
            }
        )
    }

    private fun HttpRequestBuilder.apiKeyHeader() = header("api-key", apiKey)
}
