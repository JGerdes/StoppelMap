package com.jonasgerdes.stoppelmap.model.transportation

import androidx.room.Entity
import org.threeten.bp.OffsetDateTime


@Entity(
    tableName = "departures",
    primaryKeys = ["station", "time"]
)
data class Departure(
    val station: String,
    val time: OffsetDateTime,
    val day: Int
)