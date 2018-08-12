package com.jonasgerdes.stoppelmap.model.transportation

import android.arch.persistence.room.*
import io.reactivex.Flowable


@Entity(tableName = "stations")
data class Station(
        @PrimaryKey
        val slug: String,
        val name: String,
        val route: String,
        val comment: String,
        val longitude: Double,
        val latitude: Double,
        @ColumnInfo(name = "is_return")
        val isReturnStation: Boolean
)


@Dao
interface StationDao {
    @Query("SELECT * FROM stations WHERE route like :route AND is_return = 0")
    fun getAllByRoute(route: String): List<Station>

    @Query("SELECT * FROM stations WHERE route like :route AND is_return = 1")
    fun getReturnByRoute(route: String): Station

    @Query("SELECT * FROM stations WHERE slug like :slug")
    fun getBySlug(slug: String): Station
}