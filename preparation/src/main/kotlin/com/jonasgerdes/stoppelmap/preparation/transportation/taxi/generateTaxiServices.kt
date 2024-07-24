package com.jonasgerdes.stoppelmap.preparation.transportation.taxi

import com.jonasgerdes.stoppelmap.dto.data.PhoneNumber
import com.jonasgerdes.stoppelmap.dto.data.Service
import com.jonasgerdes.stoppelmap.preparation.localizedString
import com.jonasgerdes.stoppelmap.preperation.asSlug

fun generateTaxiServices(): List<Service> = listOf(
    createTaxiService(
        name = "City-Taxi",
        phoneNumber = "+4944417788",
        phoneNumberFormatted = "04441/7788"
    ),
    createTaxiService(
        name = "Taxi Ruf 4040",
        phoneNumber = "+4944414040",
        phoneNumberFormatted = "04441/4040"
    ),
    createTaxiService(
        name = "Taxi Kolbeck",
        phoneNumber = "+4944414444",
        phoneNumberFormatted = "04441/4444"
    ),
)

fun createTaxiService(
    name: String,
    phoneNumber: String,
    phoneNumberFormatted: String,
): Service {
    return Service(
        slug = "taxi_${name.asSlug()}",
        name = localizedString(name),
        note = null,
        aliases = listOf(),
        phoneNumbers = listOf(
            PhoneNumber(
                number = phoneNumber,
                formatted = phoneNumberFormatted,
            )
        )
    )
}