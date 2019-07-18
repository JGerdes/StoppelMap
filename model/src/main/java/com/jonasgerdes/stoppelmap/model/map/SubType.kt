package com.jonasgerdes.stoppelmap.model.map

import androidx.room.Entity


@Entity(
    tableName = "sub_types",
    primaryKeys = ["slug", "name"]
)
data class SubType(
    val slug: String,
    val name: String
)
