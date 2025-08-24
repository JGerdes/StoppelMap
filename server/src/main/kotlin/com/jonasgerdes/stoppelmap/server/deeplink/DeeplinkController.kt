package com.jonasgerdes.stoppelmap.server.deeplink

import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import com.jonasgerdes.stoppelmap.server.deeplink.data.DeeplinkRepository
import com.jonasgerdes.stoppelmap.server.monitoring.Monitoring
import org.slf4j.Logger
import java.io.File

class DeeplinkController(
    private val deeplinkRepository: DeeplinkRepository,
    serverConfig: ServerConfig,
    private val monitoring: Monitoring,
    private val logger: Logger,
) {
    private val templatesDir = File(serverConfig.templatesDirectory)
    private val pageTemplate = File(templatesDir, "deeplink-template.html")

    fun getDeeplinkPage(slug: String?): String? {
        monitoring.deeplinkPreviewsOpened.increment()
        val previewData = deeplinkRepository.getPreviewData(slug) ?: return null
        return pageTemplate.readText()
            .replace("<%title%>", previewData.title)
            .replace("<%slug%>", previewData.slug ?: "")
            .replace("<%thumb%>", previewData.image ?: "https://stoppelmap.de/assets/logo-512x512.png")
    }
}