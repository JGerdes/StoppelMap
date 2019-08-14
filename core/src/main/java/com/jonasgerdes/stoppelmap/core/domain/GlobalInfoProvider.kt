package com.jonasgerdes.stoppelmap.core.domain

interface GlobalInfoProvider {

    fun getAreaBounds(): AreaBounds

    data class AreaBounds(
        val northLatitude: Double,
        val southLatitude: Double,
        val eastLongitude: Double,
        val westLongitude: Double
    )
}
