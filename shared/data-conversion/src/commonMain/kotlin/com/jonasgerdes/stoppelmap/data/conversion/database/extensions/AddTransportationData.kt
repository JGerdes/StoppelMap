package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.shared.Fee
import com.jonasgerdes.stoppelmap.data.shared.Location
import com.jonasgerdes.stoppelmap.data.transportation.Departure
import com.jonasgerdes.stoppelmap.data.transportation.DepartureType
import com.jonasgerdes.stoppelmap.data.transportation.Departure_day
import com.jonasgerdes.stoppelmap.data.transportation.Route
import com.jonasgerdes.stoppelmap.data.transportation.Station
import com.jonasgerdes.stoppelmap.data.transportation.TransportationType
import com.jonasgerdes.stoppelmap.dto.data.DepartureDay
import com.jonasgerdes.stoppelmap.dto.data.Transportation
import com.jonasgerdes.stoppelmap.dto.data.Station as DtoStation

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
                arivalStationSlug = route.arrivalStationSlug,
                type = TransportationType.Bus,
            )
        )
        addWebsites(route.slug, route.ticketWebsites)
        route.stations.forEach { station ->
            stationQueries.insert(
                Station(
                    slug = station.slug,
                    route = route.slug,
                    name = station.name,
                    annotateAsNew = station.isNew,
                    additionalInfoKey = station.additionalInfo?.let {
                        addLocalizedString(
                            it,
                            station.slug,
                            "additionalInfo"
                        )
                    }
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
            addWebsites(station.slug, station.ticketWebsites)
            station.outward.forEach { departureDay ->
                addDepartureDay(station, departureDay, DepartureType.Outward)
            }
            station.returns.forEach { departureDay ->
                addDepartureDay(station, departureDay, DepartureType.Return)
            }
        }
    }
}

private fun StoppelMapDatabase.addDepartureDay(
    station: DtoStation,
    departureDay: DepartureDay,
    type: DepartureType
) {
    val departureDaySlug = "${station.slug}_${type.id}_${departureDay.day}"
    departure_dayQueries.insert(
        Departure_day(
            slug = departureDaySlug,
            station = station.slug,
            day = departureDay.day,
            departureType = type,
            laterDeparturesOnDemand = departureDay.laterDepartureOnDemand
        )
    )
    departureDay.departures.forEach { departure ->
        departureQueries.insert(
            Departure(
                departureDay = departureDaySlug,
                time = departure.time,
                arrival = departure.arrival,
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