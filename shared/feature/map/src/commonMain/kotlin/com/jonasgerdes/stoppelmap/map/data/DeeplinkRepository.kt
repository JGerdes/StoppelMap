package com.jonasgerdes.stoppelmap.map.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map

class DeeplinkRepository {

    private val _pendingDeeplink = MutableStateFlow<String?>(null)
    val pendingDeeplink = _pendingDeeplink.asStateFlow()

    val pendingMapEntity = _pendingDeeplink.map {
        it?.removePrefix("https://")
            ?.removePrefix("http://")
            ?.removePrefix("stoppelmap.de/share/")
    }

    fun postDeeplink(url: String?) {
        _pendingDeeplink.value = url
    }

    fun clearDeeplink() {
        _pendingDeeplink.value = null
    }
}