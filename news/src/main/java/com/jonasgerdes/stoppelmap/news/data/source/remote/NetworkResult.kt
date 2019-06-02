package com.jonasgerdes.stoppelmap.news.data.source.remote

sealed class NetworkResult<out SuccessType : Any, out ErrorType : Any> {
    class Success<SuccessType: Any>(val data: SuccessType) : NetworkResult<SuccessType, Nothing>()
    data class ServerError<ErrorType: Any>(val code: Int, val error: ErrorType? = null) : NetworkResult<Nothing, ErrorType>()
    object NetworkError : NetworkResult<Nothing, Nothing>()
}