package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.dto.Locales
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.util.Version
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
class GenerateDeeplinkConfig : KoinComponent {

    private val settings: Settings by inject()
    private val version: Version by inject()

    operator fun invoke(data: StoppelMapData, deeplinkThumbnails: List<Pair<String, String>>) {
        val deeplinkConfig = DeeplinkConfig(
            versionCode = version.code,
            previewData = data.map.entities.mapNotNull { entity ->
                val title = entity.name
                    ?: entity.subType?.let { subTypeSlug ->
                        data.definitions.subTypes.firstOrNull { it.slug == subTypeSlug }?.name[Locales.de]
                    }
                    ?: data.map.typeAliases.firstOrNull { it.type == entity.type }.also {
                        if (it == null) {
                            System.err.println("No typeAlias found for $entity")
                        }
                    }?.aliases?.firstOrNull { it.locale == Locales.de }.also {
                        if (it == null) {
                            System.err.println("No german alias found for $entity")
                        }
                    }?.string

                if (title == null) null
                else entity.slug to PreviewData(
                    title = title,
                    image = deeplinkThumbnails.firstOrNull { it.first == entity.slug }?.second
                )
            }.toMap()
        )
        Json.encodeToStream(
            value = deeplinkConfig,
            stream = File(settings.staticServerDir, "deeplink-config.json").outputStream()
        )
    }

}

@Serializable
data class PreviewData(
    val title: String,
    val slug: String? = null,
    val image: String? = null
)

@Serializable
data class DeeplinkConfig(
    val versionCode: Int,
    val previewData: Map<String, PreviewData>
)