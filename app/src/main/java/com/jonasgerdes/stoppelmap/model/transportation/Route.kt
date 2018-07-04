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
) {
    object DiffCallback : DiffUtil.ItemCallback<Route>() {
        override fun areItemsTheSame(old: Route, new: Route) = old.slug == new.slug

        override fun areContentsTheSame(old: Route, new: Route) = old == new

    }
}

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

data class RouteWithStations(
        @Embedded
        val stall: Stall,
        val stations: List<Station>
)

@Dao
interface RouteDao {

    @Query("SELECT * FROM routes")
    fun getAll(): Flowable<List<Route>>
}