/*
package com.jonasgerdes.stoppelmap.model.entity

import android.arch.persistence.room.*
import io.reactivex.Flowable

@Entity(tableName = "stalls")
data class Stall(
        @PrimaryKey
        val slug: String,
        val type: String,
        val name: String? = null,
        val operator: String? = null,
        val description: String? = null,
        val priority: Int = 0,
        val isTent: Boolean = false,
        val isSearchable: Boolean = true,
        val isForKids: Boolean = false,
        val centerLon: Double? = null,
        val centerLat: Double? = null
)

@Dao
interface StallDao {

    @Query("SELECT * FROM stalls")
    fun getAllStalls(): Flowable<List<Stall>>

    @Insert
    fun insert(stall: Stall)
}*/
