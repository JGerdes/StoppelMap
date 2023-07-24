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
import io.ktor.http.ContentType
import io.ktor.http.isSuccess
import kotlinx.coroutines.TimeoutCancellationException
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


    private suspend inline fun <reified T> executeRequest(
        request: HttpClient.() -> HttpResponse
    ): Response<T> = try {
        val httpResponse = httpClient.request()
        if (httpResponse.status.isSuccess()) {
            Response.Success(body = httpResponse.body<T>())
        } else {
            Response.Error.HttpError(
                status = httpResponse.status,
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
