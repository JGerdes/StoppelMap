package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.data.*
import com.jonasgerdes.stoppelmap.preparation.Data
import com.jonasgerdes.stoppelmap.preparation.Settings
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class FillDatabase : KoinComponent {

    private val settings: Settings by inject()
    private val database: StoppelMapDatabase by inject()

    operator fun invoke() {

        val data = Data().apply {
            parseGeoJson(
                input = settings.geoJsonInput,
                output = settings.geoJsonOutput,
                descriptionFolder = null
            )

            parseEventSchedule(
                settings.manualEventsFile,
                settings.fetchedEventsFile,
            )
        }

        database.stallQueries.transaction {
            data.stalls.forEach {
                database.stallQueries.insert(
                    Stall(
                        slug = it.slug,
                        type = it.type,
                        name = it.name,
                        center_lng = it.centerLng!!,
                        center_lat = it.centerLat!!,
                        isSearchable = it.isSearchable
                    )
                )
            }
        }

        database.sub_typesQueries.transaction {
            data.subTypes.forEach {
                database.sub_typesQueries.insert(
                    Sub_types(
                        slug = it.slug,
                        name = it.name
                    )
                )
            }
        }

        database.stall_sub_typesQueries.transaction {
            data.stallSubTypes.forEach {
                database.stall_sub_typesQueries.insert(
                    Stall_sub_types(
                        stall = it.stall,
                        sub_type = it.subType
                    )
                )
            }
        }

        database.eventQueries.transaction {
            data.events.forEach {
                database.eventQueries.insert(
                    Event(
                        slug = it.slug,
                        name = it.name,
                        location = it.locationName,
                        start = it.start,
                        end = it.end,
                        description = it.description,
                        isOfficial = it.isOfficial
                    )
                )
            }
        }

        database.itemQueries.transaction {
            data.items.values.flatten().forEach {
                database.itemQueries.insert(
                    Item(
                        slug = it.slug,
                        name = it.name
                    )
                )
            }
        }

        database.stallItemsQueries.transaction {
            data.stallItems.forEach {
                database.stallItemsQueries.insert(
                    StallItems(
                        stall = it.stall,
                        item = it.item
                    )
                )
            }
        }

        database.aliasQueries.transaction {
            data.alias.forEach {
                database.aliasQueries.insert(
                    Alias(
                        alias = it.alias,
                        stall = it.stall,
                    )
                )
            }
        }

    }
}
