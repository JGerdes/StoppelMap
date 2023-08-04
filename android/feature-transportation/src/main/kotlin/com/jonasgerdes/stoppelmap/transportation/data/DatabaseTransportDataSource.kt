package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.data.model.database.PriceType
import com.jonasgerdes.stoppelmap.data.model.database.RouteType
import com.jonasgerdes.stoppelmap.transportation.DepartureQueries
import com.jonasgerdes.stoppelmap.transportation.Departure_dayQueries
import com.jonasgerdes.stoppelmap.transportation.PriceQueries
import com.jonasgerdes.stoppelmap.transportation.RouteQueries
import com.jonasgerdes.stoppelmap.transportation.StationQueries
import com.jonasgerdes.stoppelmap.transportation.model.Departure
import com.jonasgerdes.stoppelmap.transportation.model.DepartureDay
import com.jonasgerdes.stoppelmap.transportation.model.Price
import com.jonasgerdes.stoppelmap.transportation.model.Route
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.Station
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class DatabaseTransportDataSource(
    private val routeQueries: RouteQueries,
    private val stationQueries: StationQueries,
    private val priceQueries: PriceQueries,
    private val departureDayQueries: Departure_dayQueries,
    private val departureQueries: DepartureQueries,
) : TransportDataSource {
    override fun getAllRoutes(type: RouteType?): Flow<List<RouteSummary>> =
        routeQueries.getAll().asFlow().map { query ->
            query.executeAsList()
                .filter {
                    type == null || it.type == type
                }
                .map { route ->
                    RouteSummary(
                        id = route.slug,
                        title = route.title,
                        viaStations = stationQueries.getOriginStationTitlesByRoute(route.slug)
                            .executeAsList()
                            .let { stations ->
                                if (stations.size <= 4) {
                                    stations
                                } else {
                                    listOf(
                                        stations.first(),
                                        stations[stations.size / 2],
                                        stations.last(),
                                    )
                                }
                            },
                    )
                }
        }

    override fun getRouteById(id: String): Flow<Route> =
        routeQueries.getBySlug(id).asFlow().map { query ->
            val route = query.executeAsOne()
            Route(
                id = route.slug,
                title = route.title,
                stations = (stationQueries.getOriginByRoute(route.slug).executeAsList()
                        + stationQueries.getDestinationByRoute(route.slug).executeAsList())
                    .map { mapStation(it) },
                returnStations = stationQueries.getReturnByRoute(route.slug)
                    .executeAsList()
                    .map { mapStation(it) },
                type = route.type,
                additionalInfo = route.additionalInfo
            )
        }

    override fun getStationById(id: String): Flow<Station> =
        stationQueries.getBySlug(id).asFlow().map {
            mapStation(it.executeAsOne()).also {
                Timber.d(it.toString())
            }
        }

    private fun mapStation(station: com.jonasgerdes.stoppelmap.transportation.Station) =
        Station(
            id = station.slug,
            title = station.title,
            prices = priceQueries.getByStation(station.slug)
                .executeAsList()
                .map {
                    Price(
                        label = when (it.type) {
                            PriceType.Adult -> Price.PriceLabel.Adult
                            PriceType.Child -> Price.PriceLabel.Children(
                                minAge = it.minAge?.toInt(),
                                maxAge = it.maxAge?.toInt()
                            )

                            PriceType.Reduced -> Price.PriceLabel.Reduced(
                                additionalInfo = it.additionalInfo
                            )
                        },
                        amountInCents = it.amount.toInt()
                    )
                },
            isDestination = station.isDestination,
            annotateAsNew = station.annotateAsNew,
            departures = departureDayQueries.getByStation(station.slug)
                .executeAsList()
                .map {
                    DepartureDay(
                        day = it.day,
                        departures = departureQueries.get(it.id).executeAsList()
                            .map {
                                Departure(
                                    time = it.time,
                                    annotation = it.annotation_
                                )
                            }
                    )
                }

        )
}
