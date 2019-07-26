package com.jonasgerdes.stoppelmap.model.transportation

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "routes")
data class Route(
    @PrimaryKey
    val slug: String,
    val name: String
)