package com.jonasgerdes.stoppelmap.model.news.network

import com.jonasgerdes.stoppelmap.model.news.NewsResponse
import io.reactivex.Single
import retrofit2.http.GET

interface StoppelMapApi {

    @GET("news")
    fun getNews(): Single<NewsResponse>
}