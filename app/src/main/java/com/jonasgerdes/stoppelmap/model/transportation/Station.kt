package com.jonasgerdes.stoppelmap.model.transportation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import io.reactivex.Flowable


@Entity(tableName = "stations")
data class Station(
        @PrimaryKey
        val slug: String,
        val name: String,
        val route: String,
        val comment: String,
        val longitude: Double,
        val latitude: Double
)


@Dao
interface StationDao {
    @Query("SELECT * FROM stations WHERE route like :route")
    fun getAllByRoute(route: String): List<Station>
}