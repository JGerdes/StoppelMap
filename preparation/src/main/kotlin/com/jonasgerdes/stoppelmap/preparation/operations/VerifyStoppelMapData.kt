package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.preparation.Settings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class VerifyStoppelMapData : KoinComponent {

    private val settings: Settings by inject()

    operator fun invoke(data: StoppelMapData) {
        settings.descriptionFolder.listFiles().forEach { file ->
            if (data.map.entities.none { it.slug == file.nameWithoutExtension }) {
                System.err.println("ERR: No map entity for description ${file.nameWithoutExtension}")
            }
        }
    }
}