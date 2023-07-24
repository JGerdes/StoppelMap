package com.jonasgerdes.stoppelmap.dataupdate

import com.jonasgerdes.stoppelmap.base.contract.AppInfo
import com.jonasgerdes.stoppelmap.base.contract.Secrets
import com.jonasgerdes.stoppelmap.dataupdate.source.remote.CdnSource
import com.jonasgerdes.stoppelmap.dataupdate.usecase.UpdateAppConfigInBackgroundUseCase
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module
import timber.log.Timber

val dataUpdateModule = module {

    single {
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
                agent = get<AppInfo>().userAgent
            }
        }

        CdnSource(
            baseUrl = "https://cdn.stoppelmap.de/",
            httpClient = httpClient,
            apiKey = get<Secrets>().stoppelMapApiKey
        )
    }

    single {
        AppConfigRepository(
            cdnSource = get(),
            scope = get(),
        )
    }

    factory {
        UpdateAppConfigInBackgroundUseCase(
            appConfigRepository = get()
        )
    }

}
