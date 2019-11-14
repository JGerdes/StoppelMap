package com.jonasgerdes.stoppelmap.news.di

import android.content.Context
import androidx.lifecycle.ViewModel
import com.jonasgerdes.stoppelmap.core.di.ViewModelClass
import com.jonasgerdes.stoppelmap.news.data.repository.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.repository.implementation.NewsRepositoryImpl
import com.jonasgerdes.stoppelmap.news.data.source.local.NewsDatabase
import com.jonasgerdes.stoppelmap.news.data.source.remote.MoshiAdapters
import com.jonasgerdes.stoppelmap.news.data.source.remote.NewsService
import com.jonasgerdes.stoppelmap.news.data.source.remote.createNewsService
import com.jonasgerdes.stoppelmap.news.view.NewsViewModel
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import okhttp3.OkHttpClient
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
class NewsModule {

    @Singleton
    @Provides
    fun newsDatabase(context: Context): NewsDatabase = NewsDatabase.getInstance(context)

    @Singleton
    @Provides
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Singleton
    @Provides
    fun moshi(): Moshi = Moshi.Builder().add(MoshiAdapters).build()

    @Singleton
    @Provides
    fun newsService(
        okHttpClient: OkHttpClient,
        moshi: Moshi
    ): NewsService = createNewsService(okHttpClient, MoshiConverterFactory.create(moshi))

    @Singleton
    @Provides
    fun newsRepository(implementation: NewsRepositoryImpl): NewsRepository = implementation

    @Provides
    @IntoMap
    @ViewModelClass(NewsViewModel::class)
    fun newsViewModel(viewModel: NewsViewModel): ViewModel = viewModel
}