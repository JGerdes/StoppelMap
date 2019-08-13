package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.transportation.Departure
import com.jonasgerdes.stoppelmap.model.transportation.Station

@Dao
abstract class DepartureDao {
    @Query("SELECT * FROM departures where departures.station = :station")
    suspend abstract fun getAllDeparturesByStation(station: String): List<Departure>

}