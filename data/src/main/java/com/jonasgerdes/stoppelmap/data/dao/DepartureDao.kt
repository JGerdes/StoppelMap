package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.transportation.Departure
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

private val DATE_TIME_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME

@Dao
abstract class DepartureDao {
    @Query("SELECT * FROM departures where departures.station = :station")
    suspend abstract fun getAllDeparturesByStation(station: String): List<Departure>

    @Query("SELECT * FROM departures where datetime(time) > datetime(:dateTime) AND station = :station ORDER BY time ASC LIMIT :limit")
    suspend abstract fun getAllDeparturesForStationAfter(station: String, dateTime: String, limit: Int): List<Departure>

    suspend fun getAllDeparturesForStationAfter(station: String, dateTime: OffsetDateTime, limit: Int) =
        getAllDeparturesForStationAfter(station, dateTime.format(DATE_TIME_FORMAT), limit)

}