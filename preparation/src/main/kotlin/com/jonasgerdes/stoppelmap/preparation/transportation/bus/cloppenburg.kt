package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.localizedString
import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofWest
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.feldhaus
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.pricesPerTrip


internal fun cloppenburg() = createBusRoute {
    name = "Cloppenburg"
    operatorSlug = feldhaus
    arrivalStationSlug = busbahnhofWest
    additionalInfo = localizedString(
        de = "Die Haltestelle Schneiderkrug, Kreuzung entfällt dieses Jahr aufgrund einer Baustelle.",
        en = "Due to construction work, there's no stop at Schneiderkrug, Kreuzung this year."
    )

    returns {
        thursday("23:00", "00:00", "01:00", "02:30")
        friday("23:00", "00:00", "01:00", "02:00", "03:00", "04:00")
        saturday("23:00", "00:00", "01:00", "02:00", "03:00", "04:00")
        sunday("19:00", "21:00", "23:00", "01:00")
        monday {
            departures("14:30")
            "16:00" every 60.Minutes until "03:00"
        }
        tuesday("19:00", "22:45")
    }

    addStation("Cloppenburg, ZOB") {
        pricesPerTrip(oneWay = 600, roundTrip = 1100)

        outward {
            thursday("19:00", "20:00", "22:00")
            friday {
                "19:00" every 30.Minutes until "00:00"
            }
            saturday {
                "19:00" every 30.Minutes until "00:00"
            }
            sunday("16:00", "18:00", "20:00", "22:00")
            monday {
                "09:30" every 30.Minutes until "11:00"
                "11:00" every 60.Minutes until "20:00"
                "20:30" every 30.Minutes until "22:00"
            }
            tuesday("16:00", "18:00", "20:00")
        }
    }
    addStation("Westeremstek, Kühling", minutesAfterPrevious = 6) {
        pricesPerTrip(oneWay = 550, roundTrip = 950)
    }
    addStation("Emstek, Grundschule") {
        outward {
            thursday("19:10", "20:15", "22:15")
            friday {
                "19:15" every 30.Minutes until "23:15"
                "23:40" every 30.Minutes until "00:10"
            }
            saturday {
                "19:15" every 30.Minutes until "23:15"
                "23:40" every 30.Minutes until "00:10"
            }
            sunday("16:10", "18:10", "20:10", "22:10")
            monday {
                "09:45" every 30.Minutes until "11:15"
                "11:15" every 60.Minutes until "20:15"
                "20:15" every 30.Minutes until "22:15"
            }
            tuesday("16:10", "18:10", "20:10")
        }
        pricesPerTrip(oneWay = 550, roundTrip = 950)
    }
    addStation("Drantum, Emsteker Str.", minutesAfterPrevious = 5) {
        pricesPerTrip(oneWay = 500, roundTrip = 750)
    }
    addStation("Bühren, Grundschule", minutesAfterPrevious = 5) {
        pricesPerTrip(oneWay = 500, roundTrip = 750)
    }
    /*addStation("Schneiderkrug Kreuzung", minutesAfterPrevious = 4) {
        pricesPerTrip(oneWay = 500, roundTrip = 750)
    }*/
}
