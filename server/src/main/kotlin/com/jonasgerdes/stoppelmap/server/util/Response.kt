package com.jonasgerdes.stoppelmap.server.util

import io.ktor.http.HttpStatusCode

sealed interface Response<T> {
    data class Success<T>(val code: HttpStatusCode = HttpStatusCode.Companion.OK, val data: T) :
        Response<T>

    data class Error<T>(
        val code: HttpStatusCode,
        val message: String? = null
    ) : Response<T>
}