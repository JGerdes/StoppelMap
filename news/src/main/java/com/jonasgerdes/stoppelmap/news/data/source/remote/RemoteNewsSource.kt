package com.jonasgerdes.stoppelmap.news.data.source.remote

import android.util.Log
import com.squareup.moshi.Moshi
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class RemoteNewsSource @Inject constructor(
    private val newsService: NewsService,
    private val moshi: Moshi
) {

    private sealed class NextPage {
        object First : NextPage()
        data class Next(val url: String) : NextPage()
        object None : NextPage()
    }

    private val errorAdapter by lazy { moshi.adapter(Error::class.java) }

    private var nextPage: NextPage = NextPage.First

    fun resetToFirstPage() {
        nextPage = NextPage.First
    }

    suspend fun loadNextPage(): NetworkResult<NewsResponse, Error> =
        when (val page = nextPage) {
            is NextPage.First -> loadPage { newsService.getFirstPage() }
            is NextPage.Next -> loadPage { newsService.getPage(page.url) }
            is NextPage.None -> NetworkResult.ServerError(code = 404)
        }

    fun hasNextPage() = nextPage != NextPage.None

    private suspend fun loadPage(request: suspend () -> NewsResponse):
            NetworkResult<NewsResponse, Error> =
        try {
            val result = request()
            nextPage = result.pagination.next?.let { NextPage.Next(it) } ?: NextPage.None
            NetworkResult.Success(result)
        } catch (httpException: HttpException) {
            val error = parseServerError(httpException)
            NetworkResult.ServerError(httpException.code(), error)
        } catch (exception: IOException) {
            Log.e("RemoteNewsSource", "Error while loading page", exception)
            NetworkResult.NetworkError
        }

    private suspend fun parseServerError(httpException: HttpException) =
        withContext(Dispatchers.IO) {
            try {
                errorAdapter.fromJson(httpException.response()!!.toString())
            } catch (exception: IOException) {
                null
            }
        }
}