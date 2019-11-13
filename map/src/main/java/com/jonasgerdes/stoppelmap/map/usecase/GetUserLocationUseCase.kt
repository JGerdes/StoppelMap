package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.core.domain.LocationProvider
import javax.inject.Inject

class GetUserLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider
) {

    suspend operator fun invoke(): Location? = locationProvider()?.let {
        Location(latitude = it.latitude, longitude = it.longitude)
    }

    data class Location(val latitude: Double, val longitude: Double)
}