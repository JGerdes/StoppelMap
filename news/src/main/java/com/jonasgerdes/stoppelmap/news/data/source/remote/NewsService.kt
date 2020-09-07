package com.jonasgerdes.stoppelmap.news.data.source.remote

import com.jonasgerdes.stoppelmap.core.BuildConfig
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsService {
    @GET("news")
    suspend fun getFirstPage(): NewsResponse


    @GET("")
    suspend fun getPage(@Url page: String): NewsResponse
}


fun createNewsService(
    okHttpClient: OkHttpClient,
    moshiConverterFactory: MoshiConverterFactory
): NewsService {

    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(moshiConverterFactory)
        .build()
    return retrofit.create(NewsService::class.java)
}