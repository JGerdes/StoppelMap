package com.jonasgerdes.stoppelmap.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.jonasgerdes.stoppelmap.model.transportation.TransportPrice

@Dao
abstract class PriceDao {
    @Query("SELECT * FROM transport_prices where transport_prices.station = :station")
    suspend abstract fun getPricesByStation(station: String): List<TransportPrice>

}