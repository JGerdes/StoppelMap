package com.jonasgerdes.stoppelmap.shared.network.model

import io.ktor.http.HttpStatusCode

sealed interface Response<out T> {
    data class Success<out T>(val body: T) : Response<T>

    sealed interface Error : Response<Nothing> {
        data class HttpError(val status: HttpStatusCode) : Error
        data class Other(val throwable: Throwable? = null) : Error
    }
}
