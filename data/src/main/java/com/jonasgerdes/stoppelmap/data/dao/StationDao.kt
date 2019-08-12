package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.transportation.Station

@Dao
abstract class StationDao {
    @Query("SELECT * FROM stations where stations.route = :route")
    suspend abstract fun getAllStationsByRoute(route: String): List<Station>
}