package com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote

import com.jonasgerdes.stoppelmap.shared.dataupdate.io.readFully
import com.jonasgerdes.stoppelmap.shared.network.apiKeyHeader
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import com.jonasgerdes.stoppelmap.shared.network.runCatchingToResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsChannel
import okio.Sink
import okio.use

class RemoteStaticFileSource(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val apiKey: String,
) {

    suspend fun downloadFile(
        name: String,
        destination: Sink,
    ): Response<Unit> = httpClient.runCatchingToResponse<Unit>(
        request = {
            get("$baseUrl/static/$name") {
                apiKeyHeader(apiKey)
                header("api-key", apiKey)
            }
        },
        onSuccess = { response ->
            destination.use {
                response.bodyAsChannel().readFully(it)
            }
            Response.Success(Unit)
        }
    )
}
