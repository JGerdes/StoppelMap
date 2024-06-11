package com.jonasgerdes.stoppelmap.shared.network

import com.jonasgerdes.stoppelmap.shared.network.model.Response
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess
import kotlinx.coroutines.TimeoutCancellationException
import kotlin.coroutines.cancellation.CancellationException

suspend inline fun <reified T> HttpClient.executeRequest(
    request: HttpClient.() -> HttpResponse
): Response<T> = runCatchingToResponse(
    request
) {
    Response.Success(body = it.body<T>())
}

inline fun <reified T> HttpClient.runCatchingToResponse(
    request: HttpClient.() -> HttpResponse,
    onSuccess: (HttpResponse) -> Response<T>,
): Response<T> = try {
    val response = request()
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

