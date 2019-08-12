package com.jonasgerdes.stoppelmap.data.repository

import com.jonasgerdes.stoppelmap.data.StoppelmapDatabase
import com.jonasgerdes.stoppelmap.model.transportation.Route
import com.jonasgerdes.stoppelmap.model.transportation.Station

class RouteRepository(
    private val database: StoppelmapDatabase
) {

    suspend fun getAllRoutes(): List<Route> {
        return database.routeDao().getAllRoutes()
    }

    suspend fun getAllStationsForRoute(routeSlug: String): List<Station> {
        return database.stationDao().getAllStationsByRoute(routeSlug)
    }
}