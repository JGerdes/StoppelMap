package com.jonasgerdes.stoppelmap.model.map.entity

import android.arch.persistence.room.*

@Entity(tableName = "stalls")
data class Stall(
        @PrimaryKey
        val slug: String,
        val type: String,
        @ColumnInfo(name = "center_lon")
        val centerLon: Double,
        @ColumnInfo(name = "center_lat")
        val centerLat: Double,
        val name: String? = null,
        val operator: String? = null,
        val description: String? = null,
        val priority: Int = 0,
        @ColumnInfo(name = "is_tent")
        val isTent: Boolean = false,
        @ColumnInfo(name = "is_searchable")
        val isSearchable: Boolean = true,
        @ColumnInfo(name = "is_for_kids")
        val isForKids: Boolean = false
)

@Dao
interface StallDao {

    @Query("SELECT * FROM stalls")
    fun getAll(): List<Stall>

    @Query("SELECT * FROM stalls WHERE slug = :slug")
    fun getBySlug(slug: String): Stall?
}