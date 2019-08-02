package com.jonasgerdes.stoppelmap.model.map

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "stall_sub_types",
    primaryKeys = ["stall", "sub_type"])
data class StallSubType(
    val stall: String,
    @ColumnInfo(name = "sub_type")
    val subType: String
)
