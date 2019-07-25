package com.jonasgerdes.stoppelmap.model.transportation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime


@Entity(tableName = "stations")
data class Station(
        @PrimaryKey
        val slug: String,
        val name: String,
        val route: String,
        val comment: String,
        val longitude: Double?,
        val latitude: Double?,
        @ColumnInfo(name = "is_return")
        val isReturnStation: Boolean
)
