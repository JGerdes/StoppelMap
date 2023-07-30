package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun langenberg_muehlen() = createBusRoute {
    title = "Langenberg - Steinfeld - Mühlen - Kroge - Südlohne"

    addStation("Langenberg - Kirche") {
        prices(500, 200, 3 to 14)
        friday("19:00", "20:00", "21:00", "22:00")
        saturday("19:00", "20:00", "21:00", "22:00", "23:00")
        monday {
            departures("08:15")
            "10:00" every 120.Minutes until "00:00"
        }
    }
    addStation("Langenberg - Frilling", minutesAfterPrevious = 2) { prices(500, 200, 3 to 14) }
    addStation("Steinfeld - Friedlandstraße", minutesAfterPrevious = 8) {
        prices(
            500,
            200,
            3 to 14
        )
    }
    addStation("Steinfeld - Markt", minutesAfterPrevious = 5) { prices(500, 200, 3 to 14) }
    addStation("Mühlen - Grundschule", minutesAfterPrevious = 10) { prices(500, 200, 3 to 14) }
    addStation("Kroge - Grundschule", minutesAfterPrevious = 5) { prices(400, 200, 3 to 14) }
    addStation("Südlohne - Haltestelle DH -Str.", minutesAfterPrevious = 5) {
        prices(
            400,
            200,
            3 to 14
        )
    }
    addStation("Stoppelmarkt", minutesAfterPrevious = 20) { isDestination = true }


    addReturnStation {
        title = "Stoppelmarkt"
        friday {
            "00:50" every 60.Minutes until "03:50"
        }
        saturday {
            "20:00" every 60.Minutes until "00:00"
            "00:50" every 60.Minutes until "03:50"
        }
        monday {
            departures("09:15")
            "11:00" every 120.Minutes until "03:00"
        }
    }
}
