package com.jonasgerdes.stoppelmap.data.dao

import com.jonasgerdes.stoppelmap.data.limit
import com.jonasgerdes.stoppelmap.model.transportation.Departure
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

private val DATE_TIME_FORMAT = DateTimeFormatter.ISO_OFFSET_DATE_TIME

class DepartureDaoStub(private val departures: MutableList<Departure>) : DepartureDao() {

    override suspend fun getAllDeparturesByStation(station: String): List<Departure> =
        departures.filter { it.station == station }

    override suspend fun getAllDeparturesForStationAfter(
        station: String,
        dateTime: String,
        limit: Int
    ): List<Departure> =
        departures.filter {
            it.station == station
                    && it.time.isAfter(OffsetDateTime.parse(dateTime, DATE_TIME_FORMAT))
        }.limit(limit)

}