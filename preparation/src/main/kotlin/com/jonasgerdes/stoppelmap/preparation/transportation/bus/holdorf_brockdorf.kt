package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun holdorfBrockdorf() = createBusRoute {
    title = "Damme - Holdorf - Langwege - Brockdorf"

    addStation("Damme - ZOB") {
        prices(600, 200, 3 to 14)
        friday {
            "18:30" every 60.Minutes until "22:30"
        }
        saturday {
            "18:30" every 60.Minutes until "22:30"
        }
        monday {
            departures("07:55")
            "09:40" every 120.Minutes until "23:40"
        }
    }

    addStation("Holdorf - Kirche", minutesAfterPrevious = 20) {
        prices(500, 200, 3 to 14)
    }
    addStation("Langwege - A. d. Stadt", minutesAfterPrevious = 10) { prices(500, 200, 3 to 14) }
    addStation("Brockdorf - A.d. Kalvelage", minutesAfterPrevious = 10) {
        prices(
            400,
            200,
            3 to 14
        )
    }

    addStation("Stoppelmarkt", minutesAfterPrevious = 30) { isDestination = true }


    addReturnStation {
        title = "Stoppelmarkt"
        friday {
            departures("01:10", "02:10", "03:10")
        }
        saturday {
            "20:00" every 60.Minutes until "23:00"
            "00:10" every 60.Minutes until "04:10"
        }
        monday {
            "09:00" every 120.Minutes until "03:00"
        }
    }
}
