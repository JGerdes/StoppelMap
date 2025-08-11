package com.jonasgerdes.stoppelmap.server.deeplink

import com.jonasgerdes.stoppelmap.server.config.ServerConfig
import com.jonasgerdes.stoppelmap.server.deeplink.data.DeeplinkRepository
import java.io.File

class DeeplinkController(
    private val deeplinkRepository: DeeplinkRepository,
    serverConfig: ServerConfig,
) {
    private val templatesDir = File(serverConfig.templatesDirectory)
    private val pageTemplate = File(templatesDir, "deeplink-template.html")

    fun getDeeplinkPage(slug: String?): String? {
        val previewData = deeplinkRepository.getPreviewData(slug) ?: return null
        return pageTemplate.readText()
            .replace("<%title%>", previewData.title)
            .replace("<%slug%>", previewData.slug ?: "")
    }
}