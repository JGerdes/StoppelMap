package com.jonasgerdes.stoppelmap.model.map

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class Item(
        @PrimaryKey
        val slug: String,
        val name: String
)