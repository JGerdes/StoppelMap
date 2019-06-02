package com.jonasgerdes.stoppelmap.news.data.source.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jonasgerdes.stoppelmap.core.BuildConfig
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

interface NewsService {
    @GET("/news")
    fun getFirstPage(): Deferred<Response<NewsResponse>>


    @GET("")
    fun getPage(@Url page: String): Deferred<Response<NewsResponse>>
}


fun createNewsService(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): NewsService {
    val retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.API_BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(moshiConverterFactory)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
    return retrofit.create(NewsService::class.java)
}