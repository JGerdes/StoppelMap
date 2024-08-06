package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.data.transportation.DepartureType
import com.jonasgerdes.stoppelmap.data.transportation.GetDeparturesByStation
import com.jonasgerdes.stoppelmap.data.transportation.TransportationType
import com.jonasgerdes.stoppelmap.transportation.model.Departure
import com.jonasgerdes.stoppelmap.transportation.model.DepartureDay
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.StationDetails
import com.jonasgerdes.stoppelmap.transportation.model.StationSummary
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class BusRoutesRepository(
    private val transportDataSource: TransportDataSource
) {
    fun getRouteSummaries(): Flow<List<RouteSummary>> =
        transportDataSource.getRouteSummariesByType(TransportationType.Bus)

    fun getDetailedRoute(routeSlug: String) = transportDataSource.getDetailedRouteBySlug(routeSlug)

    fun getStationSummaries(routeSlug: String): Flow<List<StationSummary>> =
        transportDataSource.getStationSummariesByRoute(routeSlug)

    fun getStationDetails(stationSlug: String): Flow<StationDetails> = combine(
        transportDataSource.getDetailedStation(stationSlug),
        transportDataSource.getDepartureDaysByStation(stationSlug, DepartureType.Outward),
        transportDataSource.getDepartureDaysByStation(stationSlug, DepartureType.Return),
        transportDataSource.getFees(stationSlug),
    ) { station, outwardDepartures, returnDepartures, fees ->
        StationDetails(
            slug = station.slug,
            name = station.name,
            fees = fees.map {
                StationDetails.Fee(name = it.name, price = it.price.toInt())
            },
            additionalInfo = station.additionalInfo,
            outwardDepartures = outwardDepartures.toDepartureDay(),
            returnDepartures = returnDepartures.toDepartureDay(),
        )
    }

}

private fun List<GetDeparturesByStation>.toDepartureDay() =
    groupBy { it.slug }
        .entries
        .filter { it.value.isNotEmpty() }
        .map { (slug, departures) ->
            DepartureDay(
                slug = slug,
                day = departures.first().day,
                departures = departures.map {
                    Departure(it.time)
                }
            )
        }
