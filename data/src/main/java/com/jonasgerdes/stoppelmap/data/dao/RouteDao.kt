package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.transportation.Route

@Dao
abstract class RouteDao {
    @Query("SELECT * FROM routes")
    suspend abstract fun getAllRoutes(): List<Route>

    @Query("SELECT * FROM routes where routes.slug = :slug")
    suspend abstract fun getRouteBySlug(slug: String): Route?
}