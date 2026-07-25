package com.jonasgerdes.stoppelmap.shared.dataupdate.repository

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.dto.config.HomeCard
import com.jonasgerdes.stoppelmap.dto.config.MessageWrapper
import com.jonasgerdes.stoppelmap.dto.config.NoticeWrapper
import com.jonasgerdes.stoppelmap.dto.config.RemoteAppConfig
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.local.LocalAppConfigSource
import com.jonasgerdes.stoppelmap.shared.dataupdate.source.remote.RemoteAppConfigSource
import com.jonasgerdes.stoppelmap.shared.network.model.Response
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class AppConfigRepository(
    private val remoteAppConfigSource: RemoteAppConfigSource,
    private val localAppConfigSource: LocalAppConfigSource,
) {
    private val remoteStateFlow = MutableStateFlow<AppConfigState>(AppConfigState.Pending())
    val appConfig = combine(
        remoteStateFlow,
        localAppConfigSource.getAppConfig()
    ) { remoteState, cached ->
        when (remoteState) {
            is AppConfigState.FailedToFetch if cached != null -> AppConfigState.Available(cached)
            is AppConfigState.Pending -> AppConfigState.Pending(cache = cached)
            else -> remoteState
        }
    }
    val messages: Flow<List<MessageWrapper>> = appConfig
        .map {
            when (it) {
                is AppConfigState.Available -> it.appConfig.messages
                is AppConfigState.Pending -> it.cache?.messages ?: emptyList()
                is AppConfigState.FailedToFetch -> emptyList()
            }
        }

    val notices: Flow<List<NoticeWrapper>> = appConfig
        .map {
            when (it) {
                is AppConfigState.Available -> it.appConfig.notices ?: emptyList()
                is AppConfigState.Pending -> it.cache?.notices ?: emptyList()
                is AppConfigState.FailedToFetch -> emptyList()
            }
        }

    val homeCards: Flow<List<HomeCard>> = appConfig
        .map {
            when (it) {
                is AppConfigState.Available -> it.appConfig.homeCards ?: emptyList()
                is AppConfigState.Pending -> it.cache?.homeCards ?: emptyList()
                is AppConfigState.FailedToFetch -> emptyList()
            }
        }

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
        data class Pending(val cache: RemoteAppConfig? = null) : AppConfigState
        data object FailedToFetch : AppConfigState
        data class Available(val appConfig: RemoteAppConfig) : AppConfigState
    }
}
