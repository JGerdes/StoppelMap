package com.jonasgerdes.stoppelmap.news

import com.jonasgerdes.stoppelmap.news.data.NewsRepository
import com.jonasgerdes.stoppelmap.news.data.remote.RemoteNewsSource
import com.jonasgerdes.stoppelmap.news.ui.NewsViewModel
import io.ktor.client.*
import io.ktor.client.engine.android.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

val newsModule = module {

    single {
        HttpClient(Android) {
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
    }

    single {
        RemoteNewsSource(httpClient = get())
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
