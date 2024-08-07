package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofWest
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.feldhaus
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.pricesPerTrip


internal fun essen() = createBusRoute {
    name = "Essen"
    operatorSlug = feldhaus
    arrivalStationSlug = busbahnhofWest

    returns {
        saturday("00:00", "01:00", "02:00", "03:00")
        monday("18:30", "23:00")
    }

    addStation("Essen, Bahnhof") {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)

        outward {
            saturday("20:00", "22:00")
            monday("09:15", "17:15")
        }
    }
    addStation("Hemmelte, B68", minutesAfterPrevious = 15) {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)
    }
    addStation("Cappeln, Marktplatz", minutesAfterPrevious = 15) {
        pricesPerTrip(oneWay = 550, roundTrip = 950)
    }
    addStation("Schwichteler, ehem. Bahnhof", minutesAfterPrevious = 10) {
        pricesPerTrip(oneWay = 550, roundTrip = 950)
    }
}
