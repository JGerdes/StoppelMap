package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.dto.Locales.de
import com.jonasgerdes.stoppelmap.dto.data.BoundingBox
import com.jonasgerdes.stoppelmap.dto.data.Definitions
import com.jonasgerdes.stoppelmap.dto.data.Event
import com.jonasgerdes.stoppelmap.dto.data.Location
import com.jonasgerdes.stoppelmap.dto.data.Map
import com.jonasgerdes.stoppelmap.dto.data.MapEntity
import com.jonasgerdes.stoppelmap.dto.data.MapEntityType
import com.jonasgerdes.stoppelmap.dto.data.Route
import com.jonasgerdes.stoppelmap.dto.data.Schedule
import com.jonasgerdes.stoppelmap.dto.data.StoppelMapData
import com.jonasgerdes.stoppelmap.dto.data.Transportation
import com.jonasgerdes.stoppelmap.preparation.Settings
import com.jonasgerdes.stoppelmap.preparation.definitions.foodProducts
import com.jonasgerdes.stoppelmap.preparation.definitions.services
import com.jonasgerdes.stoppelmap.preparation.definitions.subTypes
import com.jonasgerdes.stoppelmap.preparation.definitions.tags
import com.jonasgerdes.stoppelmap.preparation.definitions.typeAliases
import com.jonasgerdes.stoppelmap.preparation.schedule.EventLocation
import com.jonasgerdes.stoppelmap.preparation.schedule.utils.htmlToText
import com.jonasgerdes.stoppelmap.preparation.transportation.generateBusRoutes
import com.jonasgerdes.stoppelmap.preparation.transportation.generateTrainRoutes
import com.jonasgerdes.stoppelmap.preparation.transportation.taxi.generateTaxiServices
import com.jonasgerdes.stoppelmap.preparation.transportation.transportOperators
import com.jonasgerdes.stoppelmap.preparation.util.Version
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File

@OptIn(ExperimentalSerializationApi::class)
class PrepareStoppelMapData : KoinComponent {

    private val settings: Settings by inject()
    private val version: Version by inject()

    operator fun invoke(): StoppelMapData {

        val parseGeoData =
            ParseGeoData(
                settings.geoJsonInput,
                File(settings.tempDir, "mapdata.geojson"),
                settings.descriptionFolder
            )

        val taxiServices = generateTaxiServices()

        parseGeoData()

        val geoEntities = parseGeoData.mapEntities.map {
            val descriptionFile = File(settings.descriptionFolder, it.slug + ".html")
            if (descriptionFile.exists()) {
                val description = descriptionFile.readText().htmlToText()
                if (description == null) it
                else it.copy(
                    description = mapOf(de to description)
                )
            } else {
                it
            }
        }

        val scheduleData = prepareSchedule(
            listOf(
                settings.manualEventsFile,
                settings.fetchedEventsFile,
            ),
            isWorkInProgress = true
        )

        val missingEventLocations = generateMissingEventLocations(
            scheduleData.events,
            parseGeoData.mapEntities,
            settings.eventLocationsFile
        )

        return StoppelMapData(
            version = version.code,
            seasonYear = 2024,
            definitions = Definitions(
                tags = tags,
                subTypes = subTypes,
                products = foodProducts,
                services = services + taxiServices,
                persons = listOf(),
                operators = transportOperators + parseGeoData.operators
            ),
            map = Map(
                entities = geoEntities + missingEventLocations,
                typeAliases = typeAliases,
                isWorkInProgress = true,
            ),
            schedule = scheduleData,
            transportation = Transportation(
                busRoutes = generateBusRoutes() + readCrawledRoutes(settings.crawledRoutesDirectory),
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
                        entry.value.htmlToText()?.let { entry.key to it }
                    }?.takeIf { it.isNotEmpty() }?.toMap()
                )
            },
            isWorkInProgress = isWorkInProgress
        )
    }

    private fun readCrawledRoutes(crawledRoutesDirectory: File): List<Route> =
        crawledRoutesDirectory
            .listFiles { f: File -> f.name.endsWith(".json") }!!
            .map {
                Json { explicitNulls = false }.decodeFromStream<Route>(it.inputStream())
            }

    private fun generateMissingEventLocations(
        events: List<Event>,
        mapEntities: List<MapEntity>,
        eventLocationsFile: File
    ): List<MapEntity> {
        val missingLocations = mutableListOf<MapEntity>()
        val eventLocations: List<EventLocation> = Json.decodeFromStream(eventLocationsFile.inputStream().buffered())

        events
            .filter { it.location != null }
            .forEach { event ->
                if (mapEntities.none { it.slug == event.location } && missingLocations.none { it.slug == event.location }) {
                    val eventLocation = eventLocations.firstOrNull { it.slug == event.location }
                    if (eventLocation == null) return@forEach
                    missingLocations += MapEntity(
                        slug = eventLocation.slug,
                        name = eventLocation.title,
                        type = MapEntityType.Misc,
                        center = Location(
                            lat = 0.0,
                            lng = 0.0,
                        ),
                        bbox = BoundingBox(
                            southLat = 0.0,
                            westLng = 0.0,
                            northLat = 0.0,
                            eastLng = 0.0,
                        ),
                        priority = 9999,
                        isSearchable = false

                    )
                }
            }

        return missingLocations.toList()
    }
}