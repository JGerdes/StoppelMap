package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.map.Stall

@Dao
abstract class StallDao {
    @Query("SELECT * FROM stalls WHERE name LIKE :name")
    suspend abstract fun findStallByName(name: String): List<Stall>
}