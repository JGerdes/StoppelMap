package com.jonasgerdes.stoppelmap.news

import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.repository.implementation.NewsRepositoryImpl
import com.jonasgerdes.stoppelmap.news.data.source.remote.RemoteNewsSource
import com.jonasgerdes.stoppelmap.news.data.source.remote.createNewsService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory


val newsModule = module {
    single { OkHttpClient.Builder().build() }
    single { Moshi.Builder().build() }
    single {
        createNewsService(
            okHttpClient = get(),
            moshiConverterFactory = MoshiConverterFactory.create(get<Moshi>())
        )
    }
    single { RemoteNewsSource(newsService = get(), moshi = get()) }
    single<NewsRepository> { NewsRepositoryImpl(remoteSource = get()) }
}