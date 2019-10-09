package com.jonasgerdes.stoppelmap.data.dao

import com.jonasgerdes.stoppelmap.model.transportation.TransportPrice

class PriceDaoStub(private val prices: MutableList<TransportPrice>) : PriceDao() {
    override suspend fun getPricesByStation(station: String): List<TransportPrice> =
        prices.filter { it.station == station }

}