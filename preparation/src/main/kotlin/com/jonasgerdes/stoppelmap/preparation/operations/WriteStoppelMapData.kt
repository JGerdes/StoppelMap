package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.preparation.Settings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@OptIn(ExperimentalSerializationApi::class)
class WriteStoppelMapData : KoinComponent {

    private val settings: Settings by inject()

    operator fun invoke(data: StoppelMapData) {
        Json.encodeToStream(
            value = data,
            stream = settings.stoppelMapDataJsonOutput.outputStream()
        )
    }
}