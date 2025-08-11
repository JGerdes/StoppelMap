package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.dto.Locales
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.preparation.Settings
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

    operator fun invoke(data: StoppelMapData) {
        val deeplinkConfig = DeeplinkConfig(
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
)

@Serializable
data class DeeplinkConfig(
    val previewData: Map<String, PreviewData>
)