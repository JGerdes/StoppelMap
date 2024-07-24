package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.shared.Fee
import com.jonasgerdes.stoppelmap.data.shared.Location
import com.jonasgerdes.stoppelmap.data.transportation.Departure
import com.jonasgerdes.stoppelmap.data.transportation.Departure_day
import com.jonasgerdes.stoppelmap.data.transportation.Route
import com.jonasgerdes.stoppelmap.data.transportation.Station
import com.jonasgerdes.stoppelmap.data.transportation.TransportationType
import com.jonasgerdes.stoppelmap.dto.data.Transportation

internal fun StoppelMapDatabase.addTransportationData(transportation: Transportation) {
    transportation.busRoutes.forEach { route ->
        routeQueries.insert(
            Route(
                slug = route.slug,
                name = route.name,
                operatorSlug = route.operator,
                additionalInfoKey = route.additionalInfo?.let {
                    addLocalizedString(
                        it,
                        route.slug,
                        "additional_info"
                    )
                },
                type = TransportationType.Bus
            )
        )
        route.stations.forEach { station ->
            stationQueries.insert(
                Station(
                    slug = station.slug,
                    route = route.slug,
                    name = station.name,
                    mapEntity = station.mapEntityLocation,
                    isDestination = station.isDestination,
                    isReturn = station.isReturn,
                    annotateAsNew = station.isNew,
                )
            )
            station.location?.let {
                locationQueries.insert(
                    Location(
                        referenceSlug = station.slug,
                        latitude = it.lat,
                        longitude = it.lng
                    )
                )
            }
            station.prices.forEachIndexed { index, fee ->
                feeQueries.insert(
                    Fee(
                        referenceSlug = station.slug,
                        nameKey = addLocalizedString(
                            fee.name,
                            station.slug,
                            "fee",
                            index.toString().padStart(2, '0'),
                            "name"
                        ),
                        price = fee.price.toLong(),
                    )
                )
            }
            station.departures.forEach { departureDay ->
                val departureDaySlug = "${station.slug}_${departureDay.day}"
                departure_dayQueries.insert(
                    Departure_day(
                        slug = departureDaySlug,
                        station = station.slug,
                        day = departureDay.day,
                        laterDeparturesOnDemand = departureDay.laterDepartureOnDemand
                    )
                )
                departureDay.departures.forEach { departure ->
                    departureQueries.insert(
                        Departure(
                            departureDay = departureDaySlug,
                            time = departure.time,
                            annotationKey = departure.annotation?.let {
                                addLocalizedString(
                                    it,
                                    departureDaySlug,
                                    departure.time.time.toString(),
                                    "annotation"
                                )
                            }
                        )
                    )
                }
            }
        }
    }
}