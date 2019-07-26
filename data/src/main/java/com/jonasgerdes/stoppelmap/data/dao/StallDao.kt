package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.map.Alias
import com.jonasgerdes.stoppelmap.model.map.Stall

@Dao
abstract class StallDao {
    @Query("SELECT * FROM stalls WHERE name LIKE :name")
    suspend abstract fun findStallByName(name: String): List<Stall>

    @Query("SELECT * FROM stalls WHERE slug = :slug")
    suspend abstract fun findStallBySlug(slug: String): Stall

    @Query("SELECT * FROM aliases WHERE alias LIKE :alias")
    suspend abstract fun findAlias(alias: String): List<Alias>


}