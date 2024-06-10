package com.jonasgerdes.stoppelmap.shared.dataupdate

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.base.model.AppInfo
import com.jonasgerdes.stoppelmap.base.model.DatabaseFile
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.base.model.Secrets
import com.jonasgerdes.stoppelmap.shared.dataupdate.io.toPath
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote.CdnSource
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.CopyAssetDataFilesUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.CopyAssetToFileUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.RemoveDatabaseFileUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.UpdateAppConfigAndDownloadFilesUseCase
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
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope
import org.koin.dsl.module

expect fun Scope.createHttpClientEngine(): HttpClientEngine
expect fun Scope.createTempPath(name: String): Path
expect fun Scope.createRemoveDatabaseUseCase(): RemoveDatabaseFileUseCase
expect fun Scope.createCopyAssetToFileUseCase(): CopyAssetToFileUseCase

val dataUpdateModule = module {

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

    single {
        CdnSource(
            baseUrl = "https://cdn.stoppelmap.de",
            httpClient = get(),
            apiKey = get<Secrets>().stoppelMapApiKey
        )
    }

    single {
        AppConfigRepository(
            cdnSource = get(),
            tempDatabase = createTempPath("temp_database.db"),
            tempMapData = createTempPath("temp_map_data.geojson")
        )
    }

    single {
        VersioningRepository(
            dataStore = PreferenceDataStoreFactory.createWithPath(
                corruptionHandler = null,
                migrations = emptyList(),
                produceFile = { get<PreferencesPathFactory>().create("versioning").toPath() },
            )
        )
    }

    factory {
        CopyAssetDataFilesUseCase(
            appInfo = get(),
            versioningRepository = get(),
            removeDatabaseFile = createRemoveDatabaseUseCase(),
            copyAssetToFile = createCopyAssetToFileUseCase(),
            databaseFile = get<DatabaseFile>().toPath(),
            mapDataFile = get<MapDataFile>().toPath(),
        )
    }

    factory {
        UpdateAppConfigAndDownloadFilesUseCase(
            appInfo = get(),
            appConfigRepository = get(),
            versioningRepository = get(),
            databaseFile = get<DatabaseFile>().toPath(),
            mapDataFile = get<MapDataFile>().toPath(),
        )
    }

}
