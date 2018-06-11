package com.jonasgerdes.stoppelmap.model.map.entity

import android.arch.persistence.room.*
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

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

@Entity(tableName = "aliases",
        primaryKeys = ["stall", "alias"])
data class Alias(
        val stall: String,
        val alias: String
)

data class StallWithAlias(
        @Embedded
        val stall: Stall,
        val alias: String
)

@Dao
interface StallDao {

    @Query("SELECT * FROM stalls")
    fun getAll(): List<Stall>

    @Query("SELECT * FROM stalls WHERE slug = :slug")
    fun getBySlug(slug: String): Stall?

    @Query("SELECT stalls.* from stalls where stalls.name like :query")
    fun searchByName(query: String): Single<List<Stall>>

    @Query("""
        SELECT stalls.*, aliases.alias
        FROM stalls
        JOIN aliases ON aliases.stall = stalls.slug
        WHERE aliases.alias LIKE :query""")
    fun searchByAlias(query: String): Single<List<StallWithAlias>>

    @Query("SELECT stalls.* FROM stall_items JOIN stalls ON stalls.slug = stall_items.stall WHERE stall_items.item = :item")
    fun getStallsByItemSlug(item: String): List<Stall>

}