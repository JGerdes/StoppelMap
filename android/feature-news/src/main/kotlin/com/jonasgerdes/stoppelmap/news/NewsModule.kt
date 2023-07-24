package com.jonasgerdes.stoppelmap.news

import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import com.jonasgerdes.stoppelmap.news.ui.NewsViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

val newsModule = module {

    val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    Timber.d(message)
                }

            }
            level = LogLevel.ALL
        }
        install(UserAgent) {
            agent = "StoppelMap Android v2022.08.28.01"
        }
    }

    single {
        RemoteNewsSource(httpClient = httpClient)
    }

    single {
        NewsRepository(remoteNewsSource = get())
    }

    viewModel {
        NewsViewModel(
            newsRepository = get()
        )
    }
}
