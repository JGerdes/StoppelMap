package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.schomaker
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun holdorfBrockdorf() = createBusRoute {
    name = "Damme - Holdorf - Langwege - Brockdorf"
    operatorSlug = schomaker
    arrivalStationSlug = busbahnhofOst

    returns {
        friday {
            departures("00:10", "01:10", "02:10", "03:10")
        }
        saturday {
            departures("00:10", "01:10", "02:10", "03:10")
        }
        monday {
            "14:10" every 120.Minutes until "02:10"
        }
    }

    addStation("Damme - ZOB") {
        prices(600, 200, 3 to 14)
        outward {
            friday("18:30", "19:30", "20:30", "21:30", "22:30")
            saturday("18:30", "19:30", "20:30", "21:30", "22:30")
            monday {
                "09:00" every 120.Minutes until "21:00"
            }
        }
    }

    addStation("Holdorf - Kirche", minutesAfterPrevious = 13) {
        prices(500, 200, 3 to 14)
    }
    addStation("Langwege - A. d. Stadt", minutesAfterPrevious = 7) { prices(500, 200, 3 to 14) }
    addStation("Brockdorf - A.d. Kalvelage", minutesAfterPrevious = 8) {
        prices(
            400,
            200,
            3 to 14
        )
    }
}
