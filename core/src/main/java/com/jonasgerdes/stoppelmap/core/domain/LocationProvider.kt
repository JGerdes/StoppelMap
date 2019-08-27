package com.jonasgerdes.stoppelmap.core.domain

interface LocationProvider {
    suspend operator fun invoke(): Location?

    data class Location(val latitude: Double, val longitude: Double)
}
