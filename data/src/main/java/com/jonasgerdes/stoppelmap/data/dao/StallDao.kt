package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.map.Alias
import com.jonasgerdes.stoppelmap.model.map.Item
import com.jonasgerdes.stoppelmap.model.map.Stall

@Dao
abstract class StallDao {
    @Query("SELECT * FROM stalls WHERE name LIKE :name")
    suspend abstract fun findStallByName(name: String): List<Stall>

    @Query("SELECT * FROM stalls WHERE slug = :slug")
    suspend abstract fun findStallBySlug(slug: String): Stall

    @Query("SELECT * FROM aliases WHERE alias LIKE :alias")
    suspend abstract fun findAlias(alias: String): List<Alias>

    @Query("SELECT * FROM stalls WHERE type LIKE :type")
    suspend abstract fun getStallsByType(type: String): List<Stall>

    @Query(
        """
        SELECT stalls.*
        FROM stall_sub_types
        JOIN stalls ON stalls.slug = stall_sub_types.stall
        WHERE stall_sub_types.sub_type = :subType
        ORDER BY stalls.name
        """
    )
    suspend abstract fun getStallsBySubType(subType: String): List<Stall>

    @Query(
        """
        SELECT stalls.*
        FROM stall_items
        JOIN stalls ON stalls.slug = stall_items.stall
        WHERE stall_items.item = :item
        ORDER BY stalls.name
        """
    )
    suspend abstract fun getStallsByItem(item: String): List<Stall>


}