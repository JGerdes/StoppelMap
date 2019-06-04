package com.jonasgerdes.stoppelmap.news.data.source.remote

import android.util.Log
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import retrofit2.Response
import java.io.IOException

class RemoteNewsSource(
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

    suspend fun loadNextPage(): NetworkResult<NewsResponse, Error> =
        when (val page = nextPage) {
            is NextPage.First -> loadPage {newsService.getFirstPage() }
            is NextPage.Next -> loadPage { newsService.getPage(page.url) }
            is NextPage.None -> NetworkResult.ServerError(code = 404)
        }

    private suspend fun loadPage(request: () -> Deferred<Response<NewsResponse>>): NetworkResult<NewsResponse, Error> {
        return try {
            val response = request().await()
            if (response.isSuccessful) {
                val result = response.body()!! //body will be != null if success
                nextPage = result.pagination.next?.let { NextPage.Next(it) } ?: NextPage.None
                NetworkResult.Success(result)
            } else {
                val error = errorAdapter.fromJson(response.errorBody()!!.string())!!
                NetworkResult.ServerError(response.code(), error)
            }
        } catch (exception: IOException) {
            Log.e("RemoteNewsSource", "Error while loading page", exception)
            NetworkResult.NetworkError
        }
    }
}