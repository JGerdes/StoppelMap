package com.jonasgerdes.stoppelmap.dto.config

import com.jonasgerdes.stoppelmap.dto.Localized
import com.jonasgerdes.stoppelmap.dto.Platform
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator
import kotlin.experimental.ExperimentalObjCName
import kotlin.native.ObjCName

@Serializable
data class RemoteAppConfig(
    val supportedVersions: SupportedVersions,
    val data: DataConfig,
    val messages: List<MessageWrapper>,
    val notices: List<NoticeWrapper>? = null,
    val homeCards: List<HomeCard>? = null,
)

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed interface HomeCard {

    val id: String get() = hashCode().toString()
    val condition: Condition?

    @Serializable
    data class Condition(
        val minVersion: Int? = null,
        val maxVersion: Int? = null,
        val version: Int? = null,
        val platform: List<Platform>? = null,
    )


    @Serializable
    @SerialName("content")
    data class Content(
        override val condition: Condition? = null,
        val headerImage: Image? = null,
        val headerImageDark: Image? = null,
        val title: Localized<String>? = null,
        val text: Localized<String>,
        val buttons: List<Button>? = null,
    ) : HomeCard {
        @OptIn(ExperimentalObjCName::class)
        @Serializable
        @ObjCName(swiftName = "ContentButton")
        data class Button(
            val icon: Icon? = null,
            val label: Localized<String>,
            val action: Action,
            val type: Type = Type.Primary,
        ) {
            @Serializable
            enum class Type {
                Primary,
                Secondary,
            }

            @Serializable
            @JsonClassDiscriminator("type")
            sealed interface Action {
                @Serializable
                @SerialName("send_feedback")
                data object SendFeedback : Action

                @Serializable
                @SerialName("open_url")
                data class OpenUrl(val url: Localized<String>) : Action

                @Serializable
                @SerialName("call_phone_number")
                data class CallPhoneNumber(val phoneNumber: String) : Action
            }
        }
    }

    @Serializable
    enum class Icon {
        Phone,
        Insta,
        Bsky,
        Masto,
        GHub,
    }

    @Serializable
    data class Image(
        val url: String,
        val blurHash: String? = null,
        val contentDescription: Localized<String>? = null
    )
}

@Serializable
data class SupportedVersions(
    val android: SupportedVersion,
    val iOS: SupportedVersion,
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
    val file: String,
    val supportedSince: SupportedSince,
) {
    @Serializable
    data class SupportedSince(
        val android: Int,
        val iOS: Int,
    )
}

@Serializable
data class MessageWrapper(
    val version: Int? = null,
    val platform: List<Platform>? = null,
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

@Serializable
data class NoticeWrapper(
    val version: Int? = null,
    val platform: List<Platform>? = null,
    val notice: Notice,
)

@Serializable
data class Notice(
    val title: Localized<String>,
    val content: Localized<String>,
    val type: Type? = Type.Info
) {
    @Serializable
    enum class Type {

        @SerialName("info")
        Info,

        @SerialName("warning")
        Warning
    }
}