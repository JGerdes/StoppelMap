package com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote

import com.jonasgerdes.stoppelmap.dto.config.RemoteAppConfig
import com.jonasgerdes.stoppelmap.shared.dataupdate.io.readFully
import com.jonasgerdes.stoppelmap.shared.network.apiKeyHeader
import com.jonasgerdes.stoppelmap.shared.network.executeRequest
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import com.jonasgerdes.stoppelmap.shared.network.runCatchingToResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import okio.FileSystem
import okio.Path
import okio.SYSTEM
import okio.use

class RemoteAppConfigSource(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val apiKey: String,
) {

    suspend fun getRemoteAppConfig(): Response<RemoteAppConfig> =
        httpClient.executeRequest<RemoteAppConfig> {
            get("$baseUrl/app-config.json") {
                apiKeyHeader(apiKey)
                header("api-key", apiKey)
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
                    apiKeyHeader(apiKey)
                    header("api-key", apiKey)
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
}
