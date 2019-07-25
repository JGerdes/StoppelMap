package com.jonasgerdes.stoppelmap.model.transportation

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(
    tableName = "transport_prices",
    primaryKeys = ["station", "type"]
)
data class TransportPrice(
    val station: String,
    val type: String,
    val price: Int
)