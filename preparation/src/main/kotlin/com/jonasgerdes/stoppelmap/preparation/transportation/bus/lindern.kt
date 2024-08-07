package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofWest
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.feldhaus
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.pricesPerTrip


internal fun lindern() = createBusRoute {
    name = "Lindern"
    operatorSlug = feldhaus
    arrivalStationSlug = busbahnhofWest

    returns {
        saturday("01:00", "02:00", "03:00")
        monday("19:00")
    }

    addStation("Lindern, Postagentur") {
        pricesPerTrip(oneWay = 700, roundTrip = 1200)

        outward {
            saturday("19:00", "21:40")
            monday("09:15")
        }
    }
    addStation("Vrees, Kirche", minutesAfterPrevious = 10) {
        pricesPerTrip(oneWay = 700, roundTrip = 1200)
    }
    addStation("Peheim, Elbers", minutesAfterPrevious = 15) {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)
    }
    addStation("Molbergen, Ort", minutesAfterPrevious = 5) {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)
    }
    addStation("Molbergen, Am Schützenplatz", minutesAfterPrevious = 10) {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)
    }
}
