package com.jonasgerdes.stoppelmap.shared.dataupdate

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.base.model.DatabaseFile
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.base.model.Secrets
import com.jonasgerdes.stoppelmap.shared.dataupdate.io.toPath
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote.RemoteAppConfigSource
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.CopyAssetDataFilesUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.CopyAssetToFileUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.RemoveDatabaseFileUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.UpdateAppConfigAndDownloadFilesUseCase
import okio.Path
import okio.Path.Companion.toPath
import org.koin.core.scope.Scope
import org.koin.dsl.module

expect fun Scope.createTempPath(name: String): Path
expect fun Scope.createRemoveDatabaseUseCase(): RemoveDatabaseFileUseCase
expect fun Scope.createCopyAssetToFileUseCase(): CopyAssetToFileUseCase

val dataUpdateModule = module {

    single {
        RemoteAppConfigSource(
            baseUrl = "https://cdn.stoppelmap.de",
            httpClient = get(),
            apiKey = get<Secrets>().stoppelMapApiKey
        )
    }

    single {
        AppConfigRepository(
            remoteAppConfigSource = get(),
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
