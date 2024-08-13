package com.jonasgerdes.stoppelmap.preparation.transportation.taxi

import com.jonasgerdes.stoppelmap.dto.data.PhoneNumber
import com.jonasgerdes.stoppelmap.dto.data.Service
import com.jonasgerdes.stoppelmap.preparation.localizedString
import com.jonasgerdes.stoppelmap.preperation.asSlug

fun generateTaxiServices(): List<Service> = listOf(
    createTaxiService(
        name = "Gabor City Taxi GmbH",
        phoneNumber = "+4944417788",
        phoneNumberFormatted = "04441 / 77 88"
    ),
    createTaxiService(
        name = "Taxi Kolbeck GmbH",
        phoneNumber = "+4944421600",
        phoneNumberFormatted = "04442 / 1600 "
    ),
    createTaxiService(
        name = "Taxi-AutoRuf Vechta",
        phoneNumber = "+4944416915",
        phoneNumberFormatted = "04441 / 6915"
    ),
    createTaxiService(
        name = "City Car Vechta",
        phoneNumber = "+4944413599",
        phoneNumberFormatted = "04441 / 3599"
    ),
    createTaxiService(
        name = "Gilan Drive Vechta e.K.",
        phoneNumber = "+4944419994544",
        phoneNumberFormatted = "04441 / 9994544"
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