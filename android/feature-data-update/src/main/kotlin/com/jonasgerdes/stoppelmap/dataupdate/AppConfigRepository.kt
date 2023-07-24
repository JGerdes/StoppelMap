package com.jonasgerdes.stoppelmap.dataupdate

import com.jonasgerdes.stoppelmap.dataupdate.model.RemoteAppConfig
import com.jonasgerdes.stoppelmap.dataupdate.model.Response
import com.jonasgerdes.stoppelmap.dataupdate.model.VersionMessage
import com.jonasgerdes.stoppelmap.dataupdate.source.remote.CdnSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

class AppConfigRepository(
    private val cdnSource: CdnSource,
    private val scope: CoroutineScope,
) {
    private val appConfigFlow = MutableStateFlow<RemoteAppConfig?>(null)
    val messages: Flow<List<VersionMessage>> = appConfigFlow
        .filterNotNull()
        .map { it.messages }

    fun updateInBackground() {
        scope.launch {
            when (val response = cdnSource.getRemoteAppConfig()) {
                is Response.Error.HttpError ->
                    Timber.e("Fetching app config failed: ${response.status}.")

                is Response.Error.Other ->
                    Timber.d("Fetching app config failed: ${response.throwable?.stackTraceToString()}")

                is Response.Success -> {
                    Timber.d("Fetching app config succeeded: ${response.body}")
                    appConfigFlow.value = response.body
                }
            }
        }
    }
}
