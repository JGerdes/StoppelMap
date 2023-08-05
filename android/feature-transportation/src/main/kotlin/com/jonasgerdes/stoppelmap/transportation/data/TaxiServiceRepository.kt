package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.model.TaxiService

class TaxiServiceRepository {

    private val services by lazy {
        listOf(
            TaxiService(
                title = "City-Taxi",
                phoneNumber = "+4944417788",
                phoneNumberFormatted = "04441/7788"
            ),
            TaxiService(
                title = "Taxi Ruf 4040",
                phoneNumber = "+4944414040",
                phoneNumberFormatted = "04441/4040"
            ),
            TaxiService(
                title = "Taxi Kolbeck",
                phoneNumber = "+4944414444",
                phoneNumberFormatted = "04441/4444"
            ),
        )
    }

    fun getTaxiServices() = services
}
