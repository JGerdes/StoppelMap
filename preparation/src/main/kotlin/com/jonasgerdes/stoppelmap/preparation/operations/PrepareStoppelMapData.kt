package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.dto.data.Definitions
import com.jonasgerdes.stoppelmap.dto.data.Event
import com.jonasgerdes.stoppelmap.dto.data.Map
import com.jonasgerdes.stoppelmap.dto.data.Schedule
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.dto.data.Transportation
import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.definitions.foodProducts
import com.jonasgerdes.stoppelmap.preparation.definitions.services
import com.jonasgerdes.stoppelmap.preparation.definitions.subTypes
import com.jonasgerdes.stoppelmap.preparation.definitions.tags
import com.jonasgerdes.stoppelmap.preparation.definitions.typeAliases
import com.jonasgerdes.stoppelmap.preparation.schedule.utils.cleanUpEventDescription
import com.jonasgerdes.stoppelmap.preparation.transportation.generateBusRoutes
import com.jonasgerdes.stoppelmap.preparation.transportation.generateTrainRoutes
import com.jonasgerdes.stoppelmap.preparation.transportation.taxi.generateTaxiServices
import com.jonasgerdes.stoppelmap.preparation.transportation.transportOperators
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
class PrepareStoppelMapData : KoinComponent {

    private val settings: Settings by inject()

    operator fun invoke(): StoppelMapData {

        val parseGeoData =
            ParseGeoData(settings.geoJsonInput, settings.geoJsonOutput, settings.descriptionFolder)

        val taxiServices = generateTaxiServices()

        parseGeoData()

        return StoppelMapData(
            version = 0, // TODO: generate version
            seasonYear = 2023,
            definitions = Definitions(
                tags = tags,
                subTypes = subTypes,
                products = foodProducts,
                services = services + taxiServices,
                persons = listOf(),
                operators = transportOperators + parseGeoData.operators
            ),
            map = Map(
                entities = parseGeoData.mapEntities,
                typeAliases = typeAliases,
                isWorkInProgress = true,
            ),
            schedule = prepareSchedule(
                listOf(
                    settings.manualEventsFile,
                    settings.fetchedEventsFile
                ),
                isWorkInProgress = true
            ),
            transportation = Transportation(
                busRoutes = generateBusRoutes(),
                trainRoutes = generateTrainRoutes(),
                taxiServices = taxiServices.map { it.slug },
                isWorkInProgress = true,
            ),
            note = null,
        )
    }

    private fun prepareSchedule(
        eventFiles: List<File>,
        isWorkInProgress: Boolean
    ): Schedule {
        val fetchedEvents: List<Event> = eventFiles.map {
            Json.decodeFromStream<List<Event>>(it.inputStream())
        }.flatten()

        return Schedule(
            events = fetchedEvents.map {
                it.copy(
                    description = it.description?.entries?.mapNotNull { entry ->
                        entry.value.cleanUpEventDescription()?.let { entry.key to it }
                    }?.takeIf { it.isNotEmpty() }?.toMap()
                )
            },
            isWorkInProgress = isWorkInProgress
        )
    }
}