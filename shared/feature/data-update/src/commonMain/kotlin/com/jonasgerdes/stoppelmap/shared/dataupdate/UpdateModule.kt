package com.jonasgerdes.stoppelmap.shared.dataupdate

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import com.jonasgerdes.stoppelmap.base.contract.PathFactory
import com.jonasgerdes.stoppelmap.base.contract.PreferencesPathFactory
import com.jonasgerdes.stoppelmap.base.model.Secrets
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.conversion.usecase.UpdateDatabaseUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.AppConfigRepository
import com.jonasgerdes.stoppelmap.shared.dataupdate.repository.DataUpdateRepository
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote.RemoteAppConfigSource
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote.RemoteStaticFileSource
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.UpdateDataUseCase
import com.jonasgerdes.stoppelmap.shared.dataupdate.usecase.UpdateRemoteAppConfigUseCase
import com.jonasgerdes.stoppelmap.shared.resources.Res
import dev.icerock.moko.resources.AssetResource
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import okio.SYSTEM
import org.koin.core.scope.Scope
import org.koin.dsl.module

expect fun Scope.bundledDataFileSystem(): FileSystem
expect fun Scope.getMapDirectory(): String
expect fun Scope.getPathForAssetResource(assetResource: AssetResource): Path

val dataUpdateModule = module {

    single {
        RemoteAppConfigSource(
            baseUrl = "https://api.stoppelmap.de",
            httpClient = get(),
            apiKey = get<Secrets>().stoppelMapApiKey
        )
    }

    single {
        RemoteStaticFileSource(
            baseUrl = "https://api.stoppelmap.de",
            httpClient = get(),
            apiKey = get<Secrets>().stoppelMapApiKey
        )
    }

    single {
        AppConfigRepository(remoteAppConfigSource = get())
    }

    single {
        DataUpdateRepository(
            dataStore = PreferenceDataStoreFactory.createWithPath(
                corruptionHandler = null,
                migrations = emptyList(),
                produceFile = { get<PreferencesPathFactory>().create("dataUpdate").toPath() },
            ),
            metadataQueries = get<StoppelMapDatabase>().metadataQueries,
            remoteStaticFileSource = get(),
            tempFileDirectory = get<PathFactory>().create("download").toPath()
        )
    }

    factory {
        UpdateRemoteAppConfigUseCase(
            appConfigRepository = get()
        )
    }

    factory {
        UpdateDataUseCase(
            appInfo = get(),
            bundledDataPath = getPathForAssetResource(Res.assets.data),
            bundledDataFileSystem = bundledDataFileSystem(),
            persistentDataDirectory = get<PathFactory>().create(getMapDirectory()).toPath(),
            dataUpdateRepository = get(),
            appConfigRepository = get(),
            updateDatabase = UpdateDatabaseUseCase(stoppelMapDatabase = get()),
            persistentFileSystem = FileSystem.SYSTEM

        )
    }

}
