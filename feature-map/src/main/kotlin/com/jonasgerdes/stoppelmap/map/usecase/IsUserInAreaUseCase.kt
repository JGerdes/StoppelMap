package com.jonasgerdes.stoppelmap.map.usecase

import android.location.Location

class IsLocationInAreaUseCase {

    private val area = AreaBounds(
        northLatitude = 52.7494011815008,
        southLatitude = 52.7429499584193,
        westLongitude = 8.28653654801576,
        eastLongitude = 8.30059127365977
    )

    operator fun invoke(location: Location): Boolean {
        return when {
            location.latitude < area.southLatitude -> false
            location.latitude > area.northLatitude -> false
            location.longitude < area.westLongitude -> false
            location.longitude > area.eastLongitude -> false
            else -> true
        }
    }

    private data class AreaBounds(
        val northLatitude: Double,
        val southLatitude: Double,
        val eastLongitude: Double,
        val westLongitude: Double
    )
}
