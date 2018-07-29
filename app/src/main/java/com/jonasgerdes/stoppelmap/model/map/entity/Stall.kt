package com.jonasgerdes.stoppelmap.model.map.entity

import android.arch.persistence.room.*
import android.support.v7.util.DiffUtil
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single

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
) {
    object DiffCallback : DiffUtil.ItemCallback<Stall>() {
        override fun areItemsTheSame(old: Stall, new: Stall) = old.slug == new.slug

        override fun areContentsTheSame(old: Stall, new: Stall) = old == new

    }
}

enum class Type(val type: String) {
    BAR("bar"),
    BUILDING("building"),
    CANDY_STALL("candy-stall"),
    EXHIBITION("exhibition"),
    FOOD_STALL("food-stall"),
    GAME_STALL("game-stall"),
    MISC("misc"),
    RESTROOM("restroom"),
    RIDE("ride"),
    SELLER_STALL("seller-stall")
}

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

    @Query("""
        SELECT stalls.*
        FROM stall_items
        JOIN stalls ON stalls.slug = stall_items.stall
        WHERE stall_items.item = :item
        """)
    fun getStallsByItemSlug(item: String): List<Stall>

    @Query("""
        SELECT stalls.*
        FROM stall_sub_types
        JOIN stalls ON stalls.slug = stall_sub_types.stall
        WHERE stall_sub_types.sub_type = :subType
        """)
    fun getStallsBySubType(subType: String): List<Stall>

    @Query("""
        SELECT *
        FROM stalls
        WHERE stalls.type = :type
        """)
    fun getStallsByType(type: String): List<Stall>

}