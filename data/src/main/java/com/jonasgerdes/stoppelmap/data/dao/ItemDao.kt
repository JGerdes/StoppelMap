package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.map.Item

@Dao
abstract class ItemDao {
    @Query("SELECT * FROM items WHERE name LIKE :name")
    suspend abstract fun findItemByName(name: String): List<Item>
}