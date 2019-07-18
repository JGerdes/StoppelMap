package com.jonasgerdes.stoppelmap.model.map

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(tableName = "stall_items",
    primaryKeys = ["stall", "item"],
    foreignKeys = [
        ForeignKey(entity = Stall::class,
            parentColumns = ["slug"],
            childColumns = ["stall"]),
        ForeignKey(entity = Item::class,
            parentColumns = ["slug"],
            childColumns = ["item"])
    ])
data class StallItem(
    val stall: String,
    val item: String
)