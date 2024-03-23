package com.jonasgerdes.stoppelmap.venue

import com.jonasgerdes.stoppelmap.base.model.VenueInformation
import kotlinx.datetime.TimeZone
import org.koin.dsl.module

val venueModule = module {
    single {
        VenueInformation(
            timeZone = TimeZone.of("Europe/Berlin")
        )
    }
}