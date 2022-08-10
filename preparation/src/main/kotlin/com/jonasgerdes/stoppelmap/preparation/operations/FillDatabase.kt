package com.jonasgerdes.stoppelmap.preparation.operations

import com.jonasgerdes.stoppelmap.data.Stall
import com.jonasgerdes.stoppelmap.data.Stall_sub_types
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.Sub_types
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
            data.subTypes.distinctBy { it.slug }.forEach {
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

    }
}
