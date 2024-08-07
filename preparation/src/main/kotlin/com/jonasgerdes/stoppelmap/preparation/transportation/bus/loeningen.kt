package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofWest
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.feldhaus
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.pricesPerTrip


internal fun loeningen() = createBusRoute {
    name = "Löningen"
    operatorSlug = feldhaus
    arrivalStationSlug = busbahnhofWest

    returns {
        saturday("01:00", "02:00", "03:00")
        monday("18:30", "23:00")
    }

    addStation("Löningen, Bahnhof") {
        pricesPerTrip(oneWay = 700, roundTrip = 1200)

        outward {
            saturday("20:00", "21:30")
            monday("09:15", "17:15")
        }
    }
    addStation("Lastrup, Pfarrer-Götting-Platz", minutesAfterPrevious = 15) {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)
    }
    addStation("Kneheim, Siedlung", minutesAfterPrevious = 15) {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)
    }
}
