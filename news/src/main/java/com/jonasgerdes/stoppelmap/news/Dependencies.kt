package com.jonasgerdes.stoppelmap.news

import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.repository.implementation.NewsRepositoryImpl
import com.jonasgerdes.stoppelmap.news.data.source.local.LocalNewsSource
import com.jonasgerdes.stoppelmap.news.data.source.local.NewsDatabase
import com.jonasgerdes.stoppelmap.news.data.source.remote.MoshiAdapters
import com.jonasgerdes.stoppelmap.news.data.source.remote.RemoteNewsSource
import com.jonasgerdes.stoppelmap.news.data.source.remote.createNewsService
import com.jonasgerdes.stoppelmap.news.usecase.GetNewsUseCase
import com.jonasgerdes.stoppelmap.news.usecase.LoadMoreNewsUseCase
import com.jonasgerdes.stoppelmap.news.usecase.RefreshNewsUseCase
import com.jonasgerdes.stoppelmap.news.view.NewsViewModel
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory


val newsModule = module {
    single { OkHttpClient.Builder().build() }
    single { Moshi.Builder().add(MoshiAdapters).build() }
    single {
        createNewsService(
            okHttpClient = get(),
            moshiConverterFactory = MoshiConverterFactory.create(get<Moshi>())
        )
    }
    single<NewsDatabase> { NewsDatabase.getInstance(context = get()) }
    single { LocalNewsSource(database = get()) }
    single { RemoteNewsSource(newsService = get(), moshi = get()) }
    single<NewsRepository> { NewsRepositoryImpl(localSource = get(), remoteSource = get()) }

    single { GetNewsUseCase(usecaseRepository = get()) }
    single { LoadMoreNewsUseCase(usecaseRepository = get()) }
    single { RefreshNewsUseCase(usecaseRepository = get()) }

    viewModel { NewsViewModel(getNews = get(), loadMoreNews = get(), refreshNews = get()) }
}