package com.jonasgerdes.stoppelmap.transportation.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOne
import com.jonasgerdes.stoppelmap.data.shared.FeeQueries
import com.jonasgerdes.stoppelmap.data.shared.WebsiteQueries
import com.jonasgerdes.stoppelmap.data.transportation.DepartureQueries
import com.jonasgerdes.stoppelmap.data.transportation.DepartureType
import com.jonasgerdes.stoppelmap.data.transportation.Departure_dayQueries
import com.jonasgerdes.stoppelmap.data.transportation.RouteQueries
import com.jonasgerdes.stoppelmap.data.transportation.StationQueries
import com.jonasgerdes.stoppelmap.data.transportation.TransportationType
import com.jonasgerdes.stoppelmap.transportation.model.DetailedRoute
import com.jonasgerdes.stoppelmap.transportation.model.RouteSummary
import com.jonasgerdes.stoppelmap.transportation.model.StationSummary
import com.jonasgerdes.stoppelmap.transportation.model.TicketWebsite
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDateTime

class TransportDataSource(
    private val routeQueries: RouteQueries,
    private val stationQueries: StationQueries,
    private val departureQueries: DepartureQueries,
    private val departureDayQueries: Departure_dayQueries,
    private val feeQueries: FeeQueries,
    private val websiteQueries: WebsiteQueries,
) {
    fun getRouteSummariesByType(type: TransportationType): Flow<List<RouteSummary>> =
        routeQueries.getBasicByType(type, ::RouteSummary)
            .asFlow()
            .mapToList(Dispatchers.IO)

    fun getDetailedRouteBySlug(routeSlug: String): Flow<DetailedRoute> =
        combine(
            routeQueries.getDetailedBySlug(routeSlug).asFlow().mapToOne(Dispatchers.IO),
            websiteQueries.getAllBySlug(routeSlug, ::TicketWebsite).asFlow().mapToList(Dispatchers.IO),
        ) { route, websites ->
            DetailedRoute(
                name = route.name,
                operatorName = route.operatorName,
                operatorSlug = route.operatorSlug,
                arrivalStationSlug = route.arivalStationSlug,
                additionalInfo = route.additionalInfo,
                ticketWebsites = websites
            )
        }

    fun getStationSummariesByRoute(routeSlug: String): Flow<List<StationSummary>> =
        stationQueries.getBasicByRoute(routeSlug, ::StationSummary)
            .asFlow()
            .mapToList(Dispatchers.IO)

    fun getStationSummariesBySlugs(stationSlugs: Set<String>): Flow<List<StationSummary>> =
        stationQueries.getBasicBySlugs(stationSlugs, ::StationSummary)
            .asFlow()
            .mapToList(Dispatchers.IO)


    suspend fun getDeparturesAfterByStation(stationSlug: String, after: LocalDateTime, limit: Long) =
        withContext(Dispatchers.IO) {
            departureQueries.getTimesAfterByStation(after = after, station = stationSlug, limit = limit)
                .executeAsList()
        }

    fun getDetailedStation(stationSlug: String) =
        stationQueries.getDetailedById(stationSlug)
            .asFlow()
            .mapToOne(Dispatchers.IO)

    fun getDepartureDaysByStation(stationSlug: String, type: DepartureType) =
        departureDayQueries.getDeparturesByStation(station = stationSlug, departureType = type)
            .asFlow()
            .mapToList(Dispatchers.IO)

    fun getFees(stationSlug: String) =
        feeQueries.getByReferenceSlugs(listOf(stationSlug))
            .asFlow()
            .mapToList(Dispatchers.IO)


}