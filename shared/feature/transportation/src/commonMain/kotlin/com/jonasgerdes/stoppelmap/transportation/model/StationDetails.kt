package com.jonasgerdes.stoppelmap.transportation.model

data class StationDetails(
    val slug: String,
    val name: String,
    val fees: List<Fee>,
    val additionalInfo: String?,
    val outwardDepartures: List<DepartureDay>,
    val returnDepartures: List<DepartureDay>,
) {
    data class Fee(
        val name: String,
        val price: Int,
    )
}