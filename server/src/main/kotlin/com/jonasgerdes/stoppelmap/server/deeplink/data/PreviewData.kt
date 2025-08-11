package com.jonasgerdes.stoppelmap.server.deeplink.data

import kotlinx.serialization.Serializable

@Serializable
data class PreviewData(
    val title: String,
    val slug: String? = null,
)

@Serializable
data class DeeplinkConfig(
    val previewData: Map<String, PreviewData>
)