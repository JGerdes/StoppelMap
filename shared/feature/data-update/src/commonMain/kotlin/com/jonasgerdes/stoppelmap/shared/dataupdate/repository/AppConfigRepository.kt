package com.jonasgerdes.stoppelmap.shared.dataupdate.repository

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.dto.config.MessageWrapper
import com.jonasgerdes.stoppelmap.dto.config.NoticeWrapper
import com.jonasgerdes.stoppelmap.dto.config.RemoteAppConfig
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.local.LocalAppConfigSource
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote.RemoteAppConfigSource
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map

class AppConfigRepository(
    private val remoteAppConfigSource: RemoteAppConfigSource,
    private val localAppConfigSource: LocalAppConfigSource,
) {
    private val remoteStateFlow = MutableStateFlow<AppConfigState>(AppConfigState.Pending)
    val appConfig = combine(
        remoteStateFlow,
        localAppConfigSource.getAppConfig()
    ) { remoteState, cached ->
        if (remoteState == AppConfigState.FailedToFetch && cached != null) {
            AppConfigState.Available(cached)
        } else {
            remoteState
        }
    }
    val messages: Flow<List<MessageWrapper>> = appConfig
        .filterIsInstance<AppConfigState.Available>()
        .map { it.appConfig.messages }

    val notices: Flow<List<NoticeWrapper>> = appConfig
        .filterIsInstance<AppConfigState.Available>()
        .map { it.appConfig.notices ?: emptyList() }

    suspend fun updateAppConfig() {
        Logger.d { "Update app config" }
        remoteStateFlow.value = when (val response = remoteAppConfigSource.getRemoteAppConfig()) {
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
                localAppConfigSource.storeAppConfig(appConfig)
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
