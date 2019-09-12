package com.jonasgerdes.stoppelmap.data.dao

import com.jonasgerdes.stoppelmap.model.transportation.Station

class StationDaoStub(private val stations: MutableList<Station>) : StationDao() {
    override suspend fun getAllStationsByRoute(route: String): List<Station> =
        stations.filter { it.route == route }

    override suspend fun getStationBySlug(slug: String): Station? =
        stations.find { it.slug == slug }
}