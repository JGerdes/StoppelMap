package com.jonasgerdes.stoppelmap.model.map

import androidx.room.Entity

@Entity(
    tableName = "items",
    primaryKeys = ["slug", "name"]
)
data class Item(
    val slug: String,
    val name: String
)