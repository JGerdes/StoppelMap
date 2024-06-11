package com.jonasgerdes.stoppelmap.shared.network

import com.jonasgerdes.stoppelmap.base.model.AppInfo
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.UserAgent
import io.ktor.client.plugins.compression.ContentEncoding
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.scope.Scope
import org.koin.dsl.module

expect fun Scope.createHttpClientEngine(): HttpClientEngine

val networkModule = module {
    single {
        HttpClient(createHttpClientEngine()) {
            install(ContentEncoding) {
                gzip()
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        co.touchlab.kermit.Logger.d { message }
                    }

                }
                level = LogLevel.ALL
            }
            install(UserAgent) {
                agent = get<AppInfo>().userAgent
            }
        }
    }
}
