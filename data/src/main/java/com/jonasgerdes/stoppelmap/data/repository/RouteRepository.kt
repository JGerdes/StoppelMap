package com.jonasgerdes.stoppelmap.data.repository

import com.jonasgerdes.stoppelmap.data.StoppelmapDatabase
import com.jonasgerdes.stoppelmap.model.transportation.Departure
import com.jonasgerdes.stoppelmap.model.transportation.Route
import com.jonasgerdes.stoppelmap.model.transportation.Station

class RouteRepository(
    private val database: StoppelmapDatabase
) {

    suspend fun getAllRoutes(): List<Route> {
        return database.routeDao().getAllRoutes()
    }

    suspend fun getRouteBySlug(slug: String): Route? {
        return database.routeDao().getRouteBySlug(slug)
    }

    suspend fun getAllStationsForRoute(routeSlug: String): List<Station> {
        return database.stationDao().getAllStationsByRoute(routeSlug)
    }

    suspend fun getStationBySlug(slug: String): Station? {
        return database.stationDao().getStationBySlug(slug)
    }

    suspend fun getDeparturesByStation(stationSlug: String): List<Departure> {
        return database.departureDao().getAllDeparturesByStation(stationSlug)
    }
}