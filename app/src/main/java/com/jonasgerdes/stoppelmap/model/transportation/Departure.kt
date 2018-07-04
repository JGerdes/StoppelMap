package com.jonasgerdes.stoppelmap.model.transportation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import org.threeten.bp.OffsetDateTime


@Entity(tableName = "departures")
data class Departure(
        @PrimaryKey
        val station: String,
        val time: OffsetDateTime
)


@Dao
interface DepartureDao {

    @Query("SELECT * FROM departures WHERE station like :station AND time > :start LIMIT :limit")
    fun getForStationAfter(station: String, start:String, limit: Int = 3): List<Departure>

}