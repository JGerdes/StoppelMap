package com.jonasgerdes.stoppelmap.model.transportation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime


@Entity(tableName = "departures")
data class Departure(
        @PrimaryKey
        val station: String,
        val time: OffsetDateTime
)