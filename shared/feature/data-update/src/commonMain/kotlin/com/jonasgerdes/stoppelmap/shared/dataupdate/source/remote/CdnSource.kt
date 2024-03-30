package com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote

import com.jonasgerdes.stoppelmap.shared.dataupdate.io.readFully
import com.jonasgerdes.stoppelmap.shared.dataupdate.model.RemoteAppConfig
import com.jonasgerdes.stoppelmap.shared.dataupdate.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.accept
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.TimeoutCancellationException
import okio.FileSystem
import okio.Path
import okio.SYSTEM
import okio.use
import kotlin.coroutines.cancellation.CancellationException

class CdnSource(
    private val baseUrl: String,
    private val httpClient: HttpClient,
    private val apiKey: String,
) {

    suspend fun getRemoteAppConfig(): Response<RemoteAppConfig> =
        executeRequest {
            get("$baseUrl/app-config.json") {
                apiKeyHeader()
                accept(ContentType.Application.Json)
            }
        }

    suspend fun downloadFile(
        destination: Path,
        name: String,
    ): Response<Path> {
        return runCatchingToResponse(
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

    private suspend inline fun <reified T> executeRequest(
        request: HttpClient.() -> HttpResponse
    ): Response<T> = runCatchingToResponse(
        request
    ) {
        Response.Success(body = it.body<T>())
    }

    private inline fun <reified T> runCatchingToResponse(
        request: HttpClient.() -> HttpResponse,
        onSuccess: (HttpResponse) -> Response<T>,
    ): Response<T> = try {
        val response = httpClient.request()
        if (response.status.isSuccess()) {
            try {
                onSuccess(response)
            } catch (cancellationException: CancellationException) {
                throw cancellationException
            } catch (throwable: Throwable) {
                Response.Error.Other(throwable)
            }
        } else {
            Response.Error.HttpError(
                status = response.status,
            )
        }
    } catch (timeoutCancellationException: TimeoutCancellationException) {
        Response.Error.Other()
    } catch (cancellationException: CancellationException) {
        throw cancellationException
    } catch (throwable: Throwable) {
        Response.Error.Other(throwable)
    }


    private fun HttpRequestBuilder.apiKeyHeader() = header("api-key", apiKey)
}
