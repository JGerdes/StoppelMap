package com.jonasgerdes.stoppelmap.server.deeplink.data

import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import com.jonasgerdes.stoppelmap.server.scheduler.ClockProvider
import kotlinx.datetime.Instant
import kotlinx.io.IOException
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.slf4j.Logger
import java.io.File
import kotlin.time.Duration.Companion.minutes

private val configCacheDuration = 10.minutes

class DeeplinkRepository(
    private val serverConfig: ServerConfig,
    private val json: Json,
    private val clockProvider: ClockProvider,
    private val logger: Logger,
) {
    private var config: DeeplinkConfig? = null
    private var lastReload: Instant? = null
    private var currentConfigModifiedDate: Long? = null

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
        val fileLastModified = file.lastModified()
        if (file.lastModified() <= (currentConfigModifiedDate ?: 0)) {
            logger.info("Deeplink: Config updated as file hasn't changed: ${file.absolutePath}")
            return
        }
        logger.info("Deeplink: Reload config from ${file.absolutePath}")
        try {
            config = json.decodeFromStream<DeeplinkConfig>(file.inputStream())
            currentConfigModifiedDate = fileLastModified
            logger.info("Deeplink: Updated config to version ${config?.versionCode}")
        } catch (se: SerializationException) {
            logger.error("Deeplink: Failed to parse JSON of latest config file: ${se.message}", se)
        } catch (iae: IllegalArgumentException) {
            logger.error("Deeplink: Failed to parse JSON of latest config file: ${iae.message}", iae)
        } catch (ioe: IOException) {
            logger.error("Deeplink: Failed to open file: ${ioe.message}", ioe)
        }
    }
}