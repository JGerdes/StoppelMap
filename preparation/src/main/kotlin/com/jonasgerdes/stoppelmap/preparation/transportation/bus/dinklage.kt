package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun dinklage() = createBusRoute {
    title = "Dinlage - Märschendorf"

    addStation("Rathausplatz") {
        prices(500, 200, 3 to 14)
        thursday {
            "19:00" every 60.Minutes until "23:00"
        }
        friday {
            departures("15:00", "16:15", "17:45")
            "19:00" every 30.Minutes until "00:00"
        }
        saturday {
            departures("14:00", "15:15", "16:30", "17:45")
            "19:00" every 30.Minutes until "00:00"
        }
        sunday {
            departures("13:30", "15:00", "16:45", "18:15", "19:20", "20:40", "21:45", "23:00")
        }
        monday {
            "08:15" every 45.Minutes until "23:15"
        }
        tuesday {
            departures("15:00", "16:20", "18:00", "19:10", "20:30")
        }
    }

    addStation("In der Wiek", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Schulstraße", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Clemens-August-Str./ Hörster Allee", minutesAfterPrevious = 3) {
        prices(
            500,
            200,
            3 to 14
        )
    }
    addStation("Bahnhof", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Grundschule Höner Mark", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Sanderstr./ Feuerwehr", minutesAfterPrevious = 3) { prices(500, 200, 3 to 14) }
    addStation("Märschendorf - Gastw. Eveslage", minutesAfterPrevious = 3) {
        prices(
            400,
            200,
            3 to 14
        )
    }
    addStation("Stoppelmarkt", minutesAfterPrevious = 14) { isDestination = true }


    addReturnStation {
        title = "Stoppelmarkt"
        thursday {
            "19:50" every 60.Minutes until "23:50"
            departure("01:10")
        }
        friday {
            departures("15:50", "17:10", "18:40")
            "19:55" every 30.Minutes until "04:55"
        }
        saturday {
            departures("14:40", "15:55", "17:20", "18:40")
            "19:55" every 30.Minutes until "04:55"
        }
        sunday {
            departures(
                "14:25",
                "15:55",
                "17:40",
                "19:10",
                "20:10",
                "21:30",
                "22:40",
                "00:10",
                "01:25"
            )
        }
        monday {
            "09:05" every 45.Minutes until "03:05"
        }
        tuesday {
            departures("15:50", "17:20", "18:40", "20:00", "21:10", "22:25", "01:10")
        }
    }
}
