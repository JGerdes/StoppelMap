package com.jonasgerdes.stoppelmap.server.deeplink.data

import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import com.jonasgerdes.stoppelmap.server.scheduler.ClockProvider
import kotlinx.datetime.Instant
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.File
import kotlin.time.Duration.Companion.minutes

private val configCacheDuration = 5.minutes

class DeeplinkRepository(
    private val serverConfig: ServerConfig,
    private val json: Json,
    private val clockProvider: ClockProvider,
) {
    private var config: DeeplinkConfig? = null
    private var lastReload: Instant? = null

    fun getPreviewData(slug: String?): PreviewData? {
        if (slug == null) return null
        reloadConfigIfNecessary()
        return config?.previewData[slug]?.copy(slug = slug)
    }

    private fun reloadConfigIfNecessary() {
        val lastReload = lastReload
        if (lastReload == null || clockProvider.now() - lastReload > configCacheDuration) {
            reloadConfig()
        }
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun reloadConfig() {
        val file = File(serverConfig.deeplinkConfigFile)
        config = json.decodeFromStream<DeeplinkConfig>(file.inputStream())
    }
}