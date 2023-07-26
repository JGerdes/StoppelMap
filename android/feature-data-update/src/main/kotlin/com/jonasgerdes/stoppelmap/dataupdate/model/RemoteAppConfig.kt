package com.jonasgerdes.stoppelmap.dataupdate.model

import com.jonasgerdes.stoppelmap.base.contract.Localized
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteAppConfig(
    val supportedVersions: SupportedVersions,
    val data: DataConfig,
    val messages: List<VersionMessage>
)

@Serializable
data class SupportedVersions(
    val android: SupportedVersion,
)

@Serializable
data class SupportedVersion(
    val oldest: Int,
)

@Serializable
data class DataConfig(
    val latest: Data,
)


@Serializable
data class Data(
    val version: Int,
    val data: String,
    val map: String,
    val supportedSince: SupportedSince,
) {
    @Serializable
    data class SupportedSince(
        val android: Int,
    )
}


@Serializable
data class VersionMessage(
    val version: Int? = null,
    val message: Message,
)

@Serializable
data class Message(
    val title: Localized<String>,
    val content: Localized<String>,
    val type: Type? = null,
    val buttons: List<Button> = emptyList(),
) {
    @Serializable
    data class Button(
        val title: Localized<String>,
        val url: Localized<String>,
    )

    @Serializable
    enum class Type {

        @SerialName("info")
        Info,

        @SerialName("warning")
        Warning
    }
}
