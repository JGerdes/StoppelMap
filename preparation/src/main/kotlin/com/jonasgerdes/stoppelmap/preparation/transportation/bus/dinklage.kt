package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.schomaker
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun dinklage() = createBusRoute {
    name = "Dinlage - Märschendorf"
    operatorSlug = schomaker
    arrivalStationSlug = busbahnhofOst

    returns {
        thursday {
            departures("21:50", "22:50", "23:50", "00:50")
        }
        friday {
            departures("17:05", "18:35")
            "19:05" every 30.Minutes until "03:35"
        }
        saturday {
            departures("17:20", "18:35")
            "19:05" every 30.Minutes until "03:35"
        }
        sunday {
            departures("16:05", "17:35", "19:05", "20:35", "21:55", "23:10")
        }
        monday {
            "12:35" every 30.Minutes until "02:05"
        }
        tuesday {
            departures("17:35", "19:05", "20:35", "21:55", "23:10", "00:35", "02:00")
        }
    }

    addStation("Rathausplatz") {
        prices(500, 200, 3 to 14)
        outward {
            thursday("19:00", "20:00", "21:00", "22:00")
            friday {
                departures("15:00", "16:15", "17:45")
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

    addStation("In der Wiek", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Schulstraße", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Clemens-August-Str./Hörster Allee", minutesAfterPrevious = 3) {
        prices(
            500,
            200,
            3 to 14
        )
    }
    addStation("Bahnhof", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Grundschule Höner Mark", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Sanderstr./Feuerwehr", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Märschendorf - Gastw. Eveslage", minutesAfterPrevious = 3) {
        prices(
            400,
            200,
            3 to 14
        )
    }
}
