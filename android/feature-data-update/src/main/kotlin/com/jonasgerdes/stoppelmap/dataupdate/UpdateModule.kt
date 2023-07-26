package com.jonasgerdes.stoppelmap.dataupdate

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.jonasgerdes.stoppelmap.base.contract.AppInfo
import com.jonasgerdes.stoppelmap.base.contract.DatabaseFile
import com.jonasgerdes.stoppelmap.base.contract.MapDataFile
import com.jonasgerdes.stoppelmap.base.contract.Secrets
import com.jonasgerdes.stoppelmap.dataupdate.source.remote.CdnSource
import com.jonasgerdes.stoppelmap.dataupdate.usecase.CopyAssetDataFilesUseCase
import com.jonasgerdes.stoppelmap.dataupdate.usecase.UpdateAppConfigAndDownloadFilesUseCase
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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "versioning")

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
            baseUrl = "https://cdn.stoppelmap.de",
            httpClient = httpClient,
            apiKey = get<Secrets>().stoppelMapApiKey
        )
    }

    single {
        AppConfigRepository(
            cdnSource = get(),
            cacheDir = get<Context>().cacheDir
        )
    }

    single {
        VersioningRepository(dataStore = get<Context>().dataStore)
    }

    factory {
        CopyAssetDataFilesUseCase(
            appInfo = get(),
            versioningRepository = get(),
            context = get(),
            databaseFile = get<DatabaseFile>().databaseFile,
            mapDataFile = get<MapDataFile>().mapDataFile,
        )
    }

    factory {
        UpdateAppConfigAndDownloadFilesUseCase(
            appInfo = get(),
            appConfigRepository = get(),
            versioningRepository = get(),
            databaseFile = get<DatabaseFile>().databaseFile,
            mapDataFile = get<MapDataFile>().mapDataFile,
        )
    }

}
