package com.jonasgerdes.stoppelmap.model.map

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stalls")
data class Stall(
    @PrimaryKey
        val slug: String,
    val type: Type,
    @ColumnInfo(name = "center_lng")
        val centerLng: Double,
    @ColumnInfo(name = "center_lat")
        val centerLat: Double,
    @ColumnInfo(name = "min_lng")
        val minLng: Double,
    @ColumnInfo(name = "min_lat")
        val minLat: Double,
    @ColumnInfo(name = "max_lng")
        val maxLng: Double,
    @ColumnInfo(name = "max_lat")
        val maxLat: Double,
    val name: String? = null,
    val operator: String? = null,
    val description: String? = null,
    val priority: Int = 0,
    @ColumnInfo(name = "is_searchable")
        val isSearchable: Boolean = true
)