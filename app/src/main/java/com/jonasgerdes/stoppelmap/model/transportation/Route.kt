package com.jonasgerdes.stoppelmap.model.transportation

import android.arch.persistence.room.*
import android.support.v7.util.DiffUtil
import com.jonasgerdes.stoppelmap.model.map.entity.Stall
import io.reactivex.Flowable

@Entity(tableName = "routes")
data class Route(
        @PrimaryKey
        val slug: String,
        val name: String
)

@Dao
interface RouteDao {
    @Query("SELECT * FROM routes")
    fun getAll(): Flowable<List<Route>>
}