package com.jonasgerdes.stoppelmap.shared.dataupdate.repository

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.dto.config.MessageWrapper
import com.jonasgerdes.stoppelmap.dto.config.RemoteAppConfig
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote.RemoteAppConfigSource
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

class AppConfigRepository(
    private val remoteAppConfigSource: RemoteAppConfigSource,
) {
    private val appConfigFlow = MutableStateFlow<AppConfigState>(AppConfigState.Pending)
    val appConfig = appConfigFlow.asStateFlow()
    val messages: Flow<List<MessageWrapper>> = appConfigFlow
        .filterIsInstance<AppConfigState.Available>()
        .map { it.appConfig.messages }

    suspend fun updateAppConfig() {
        Logger.d { "Update app config" }
        appConfigFlow.value = when (val response = remoteAppConfigSource.getRemoteAppConfig()) {
            is Response.Error.HttpError -> {
                Logger.e { "Fetching app config failed: ${response.status}." }
                AppConfigState.FailedToFetch
            }

            is Response.Error.Other -> {
                Logger.d { "Fetching app config failed: ${response.throwable?.stackTraceToString()}" }
                AppConfigState.FailedToFetch
            }

            is Response.Success -> {
                val appConfig = response.body
                Logger.d { "Fetching app config succeeded: $appConfig" }
                AppConfigState.Available(appConfig)
            }
        }
    }

    sealed interface AppConfigState {
        data object Pending : AppConfigState
        data object FailedToFetch : AppConfigState
        data class Available(val appConfig: RemoteAppConfig) : AppConfigState
    }
}
