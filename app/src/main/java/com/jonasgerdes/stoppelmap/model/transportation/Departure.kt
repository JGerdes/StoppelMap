package com.jonasgerdes.stoppelmap.model.transportation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import com.jonasgerdes.stoppelmap.model.Converters
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.temporal.ChronoUnit


@Entity(tableName = "departures")
data class Departure(
        @PrimaryKey
        val station: String,
        val time: OffsetDateTime
) {
    fun millisUntil(until: OffsetDateTime) = ChronoUnit.MILLIS.between(until, time)
}


@Dao
interface DepartureDao {

    @Query("SELECT * FROM departures WHERE station like :station AND time > :start LIMIT :limit")
    fun getForStationAfter(station: String, start: String, limit: Int = 3): List<Departure>

}

fun DepartureDao.getForStationAfter(station: String, start: OffsetDateTime, limit: Int = 3) =
        getForStationAfter(station, Converters.fromOffsetDateTime(start)!!, limit)