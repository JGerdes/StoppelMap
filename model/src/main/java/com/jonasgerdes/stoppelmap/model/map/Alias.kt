package com.jonasgerdes.stoppelmap.model.map

import androidx.room.Entity

@Entity(tableName = "aliases",
    primaryKeys = ["stall", "alias"])
data class Alias(
    val stall: String,
    val alias: String
)