package com.jonasgerdes.stoppelmap.data.dao

import com.jonasgerdes.stoppelmap.model.transportation.Route

class RouteDaoStub(private val routes: MutableList<Route>) : RouteDao() {
    override suspend fun getAllRoutes(): List<Route> = routes

    override suspend fun getRouteBySlug(slug: String): Route? = routes.first { it.slug == slug }
}