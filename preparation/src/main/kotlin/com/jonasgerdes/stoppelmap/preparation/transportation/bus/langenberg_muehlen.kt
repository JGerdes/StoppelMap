package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.schomaker
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun langenbergMuehlen() = createBusRoute {
    name = "Langenberg - Steinfeld - Mühlen - Kroge - Südlohne"
    operatorSlug = schomaker
    arrivalStationSlug = busbahnhofOst

    returns {
        friday {
            "00:50" every 60.Minutes until "03:50"
        }
        saturday {
            "22:00" every 60.Minutes until "00:00"
            "00:50" every 60.Minutes until "03:50"
        }
        monday {
            "12:00" every 120.Minutes until "02:00"
        }
    }

    addStation("Langenberg - Kirche") {
        prices(500, 200, 3 to 14)
        outward {
            friday("19:00", "20:00", "21:00", "22:00")
            saturday("19:00", "20:00", "21:00", "22:00", "23:00")
            monday {
                "09:00" every 120.Minutes until "21:00"
            }
        }
    }
    addStation("Langenberg - Frilling", minutesAfterPrevious = 2) { prices(500, 200, 3 to 14) }
    addStation("Steinfeld - Friedlandstraße", minutesAfterPrevious = 5) {
        prices(
            500,
            200,
            3 to 14
        )
    }
    addStation("Steinfeld - Markt", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Mühlen - Grundschule", minutesAfterPrevious = 10) { prices(500, 200, 3 to 14) }
    addStation("Kroge - Grundschule", minutesAfterPrevious = 5) { prices(400, 200, 3 to 14) }
    addStation("Südlohne - Haltestelle DH-Str.", minutesAfterPrevious = 5) {
        prices(
            400,
            200,
            3 to 14
        )
    }
}
