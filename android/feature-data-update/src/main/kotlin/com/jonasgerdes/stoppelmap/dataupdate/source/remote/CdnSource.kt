package com.jonasgerdes.stoppelmap.dataupdate.source.remote

import com.jonasgerdes.stoppelmap.dataupdate.model.RemoteAppConfig
import com.jonasgerdes.stoppelmap.dataupdate.model.Response
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
import io.ktor.util.cio.writeChannel
import io.ktor.utils.io.copyAndClose
import kotlinx.coroutines.TimeoutCancellationException
import java.io.File
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
        destinationDirectory: File,
        name: String,
    ): Response<File> {
        val file = File(destinationDirectory, name)
        return runCatchingToResponse(
            request = {
                get("$baseUrl/$name") {
                    apiKeyHeader()
                }
            },
            onSuccess = {
                it.bodyAsChannel().copyAndClose(file.writeChannel())
                Response.Success(file)
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
