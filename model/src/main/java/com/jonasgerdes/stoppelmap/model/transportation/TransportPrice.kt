package com.jonasgerdes.stoppelmap.model.transportation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import org.threeten.bp.OffsetDateTime


@Entity(tableName = "transport_prices")
data class TransportPrice(
        @PrimaryKey
        val station: String,
        val type: String,
        val price: Int
)