package com.jonasgerdes.stoppelmap.data.conversion.usecase

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.conversion.database.extensions.addDefinitions
import com.jonasgerdes.stoppelmap.data.conversion.database.extensions.addMapData
import com.jonasgerdes.stoppelmap.data.conversion.database.extensions.addScheduleData
import com.jonasgerdes.stoppelmap.data.conversion.database.extensions.addTransportationData
import com.jonasgerdes.stoppelmap.data.conversion.database.extensions.clearData
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData

class UpdateDatabaseUseCase(
    private val stoppelMapDatabase: StoppelMapDatabase,
) {
    operator fun invoke(
        data: StoppelMapData,
    ) = with(stoppelMapDatabase) {
        transaction {
            clearData()

            metadataQueries.insert(
                version = data.version.toLong(),
                schema_version = data.schemaVersion.toLong(),
                note = data.note,
                season_year = data.seasonYear.toLong(),
                map_is_wip = data.map.isWorkInProgress,
                schedule_is_wip = data.schedule.isWorkInProgress,
                transport_is_wip = data.transportation.isWorkInProgress,
            )

            addDefinitions(data.definitions)
            addMapData(data.map)
            addScheduleData(data.schedule)
            addTransportationData(data.transportation)
        }
    }
}
