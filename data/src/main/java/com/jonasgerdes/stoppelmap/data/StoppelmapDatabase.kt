package com.jonasgerdes.stoppelmap.data

import com.jonasgerdes.stoppelmap.data.dao.*

interface StoppelmapDatabase {
    fun stallsDao(): StallDao
    fun stallTypeDao(): StallTypeDao
    fun itemDao(): ItemDao
    fun eventDao(): EventDao
    fun routeDao(): RouteDao
    fun stationDao(): StationDao
    fun departureDao(): DepartureDao
    fun priceDao(): PriceDao
}