package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.map.Alias
import com.jonasgerdes.stoppelmap.model.map.Stall
import com.jonasgerdes.stoppelmap.model.map.SubType

@Dao
abstract class StallTypeDao {
    @Query("SELECT * FROM sub_types WHERE name LIKE :name")
    suspend abstract fun findTypeByName(name: String): List<SubType>

    @Query("SELECT * FROM sub_types WHERE slug LIKE :slug")
    suspend abstract fun getTypeBySlug(slug: String): SubType?


}