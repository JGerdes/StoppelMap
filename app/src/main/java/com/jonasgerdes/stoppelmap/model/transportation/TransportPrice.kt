package com.jonasgerdes.stoppelmap.model.transportation

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.Query
import io.reactivex.Flowable


@Entity(tableName = "transport_prices")
data class TransportPrice(
        @PrimaryKey
        val station: String,
        val type: String,
        val price: Int
)


@Dao
interface TransportPriceDao {
    @Query("SELECT * FROM transport_prices WHERE station like :station")
    fun getAllByStation(station: String): List<TransportPrice>
}