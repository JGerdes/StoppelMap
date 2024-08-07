package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofWest
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.feldhaus
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.pricesPerTrip


internal fun friesoythe() = createBusRoute {
    name = "Friesoythe"
    operatorSlug = feldhaus
    arrivalStationSlug = busbahnhofWest

    returns {
        saturday("01:00", "02:00", "03:00")
        monday("18:00")
    }

    addStation("Friesoythe, Hansaplatz") {
        pricesPerTrip(oneWay = 700, roundTrip = 1200)

        outward {
            saturday("19:15", "21:45")
            monday("09:20")
        }
    }
    addStation("Bösel, Kath. Kirche", minutesAfterPrevious = 10) {
        pricesPerTrip(oneWay = 700, roundTrip = 1200)
    }
    addStation("Garrel, Kirche", minutesAfterPrevious = 10) {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)
    }
    addStation("Beverbruch, GS/KiGa", minutesAfterPrevious = 10) {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)
    }
    addStation("Kellerhöhe, Kirche", minutesAfterPrevious = 5) {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)
    }
    addStation("Höltinghausen, Ort", minutesAfterPrevious = 10) {
        pricesPerTrip(oneWay = 550, roundTrip = 950)
    }
    addStation("Halen, Eichfeldsiedlung", minutesAfterPrevious = 10) {
        pricesPerTrip(oneWay = 550, roundTrip = 950)
    }
}
