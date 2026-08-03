package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.schomaker
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices
import com.jonasgerdes.stoppelmap.preparation.transportation.schomakerSundayDealPrices

internal fun dinklage() = createBusRoute {
    name = "Dinlage - Märschendorf"
    operatorSlug = schomaker
    arrivalStationSlug = busbahnhofOst

    returns {
        thursday {
            departures("21:50", "22:50", "23:50", "00:50")
        }
        friday {
            departures("17:10", "18:40")
            "19:35" every 30.Minutes until "03:35"
        }
        saturday {
            departures("17:10", "18:25")
            "19:35" every 30.Minutes until "03:35"
        }
        sunday {
            departures("16:05", "17:35", "19:05", "20:35", "22:05", "23:35")
        }
        monday {
            "12:35" every 30.Minutes until "00:05"
        }
        tuesday {
            departures("17:35", "19:05", "20:35", "21:55", "23:10", "00:35")
        }
    }

    addStation("Rathausplatz") {
        prices(600, 250, 3 to 14, schomakerSundayDealPrices)
        outward {
            thursday("19:00", "20:00", "21:00", "22:00")
            friday {
                departures("15:00", "16:30", "18:00")
                "19:00" every 30.Minutes until "23:00"
            }
            saturday {
                departures("14:00", "15:15", "16:30", "17:45")
                "19:00" every 30.Minutes until "23:00"
            }
            sunday {
                departures("13:30", "15:00", "16:30", "18:00", "19:30", "21:00")
            }
            monday {
                "09:00" every 30.Minutes until "22:00"
            }
            tuesday {
                departures("15:00", "16:30", "18:00", "19:30", "21:00")
            }
        }
    }

    addStation("In der Wiek", minutesAfterPrevious = 3) {
        prices(600, 250, 3 to 14, schomakerSundayDealPrices)
    }
    addStation("Schulzentrum", minutesAfterPrevious = 3) {
        prices(600, 250, 3 to 14, schomakerSundayDealPrices)
    }
    addStation("Hörster Allee", minutesAfterPrevious = 3) {
        prices(600, 250, 3 to 14, schomakerSundayDealPrices)
    }
    addStation("Bahnhof", minutesAfterPrevious = 3) {
        prices(600, 250, 3 to 14, schomakerSundayDealPrices)
    }
    addStation("Grundschule Höner Mark", minutesAfterPrevious = 3) {
        prices(600, 250, 3 to 14, schomakerSundayDealPrices)
    }
    addStation("Sanderstraße/Groneik", minutesAfterPrevious = 3) {
        prices(600, 250, 3 to 14, schomakerSundayDealPrices)
    }
    addStation("Märschendorf - Gastw. Eveslage", minutesAfterPrevious = 3) {
        prices(500, 250, 3 to 14, schomakerSundayDealPrices)
    }
}
