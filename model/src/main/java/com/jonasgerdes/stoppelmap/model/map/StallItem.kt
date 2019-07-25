package com.jonasgerdes.stoppelmap.model.map

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "stall_items",
    primaryKeys = ["stall", "item"],
    foreignKeys = [
        ForeignKey(entity = Stall::class,
            parentColumns = ["slug"],
            childColumns = ["stall"])
    ])
data class StallItem(
    val stall: String,
    val item: String
)