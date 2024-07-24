package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun lohneStadt() = createBusRoute {
    name = "Lohne Stadt"
    fixedPrices = prices(adult = 400, children = 200, 3 to 14)

    addStation("Busbahnhof, Falkenbergstraße") {
        thursday {
            "17:00" every 30.Minutes until "00:00"
        }
        friday {
            "14:00" every 30.Minutes until "19:00"
            "19:00" every 15.Minutes until "20:00"
            "20:00" every 10.Minutes until "01:00"
        }
        saturday {
            "13:00" every 20.Minutes until "19:00"
            "19:00" every 15.Minutes until "20:00"
            "20:00" every 10.Minutes until "01:00"
        }
        sunday {
            "13:00" every 30.Minutes until "00:30"
        }
        monday {
            "08:00" every 15.Minutes until "00:00"
        }
        tuesday {
            "14:00" every 30.Minutes until "23:00"
        }
    }


    addStation("Brinkstraße, ehem. Möbel Kröger", minutesAfterPrevious = 3)
    addStation("Hamberg, Haltestelle", minutesAfterPrevious = 3)
    addStation("Bergweg, Bruno Kleine", minutesAfterPrevious = 3)
    addStation("Bergweg, Felta-Tankstelle", minutesAfterPrevious = 2)
    addStation("Brägeler Straße, Imbiß Zerhusen", minutesAfterPrevious = 2)
    addStation("Lindenstraße, Kino", minutesAfterPrevious = 2)
    addStation("Lindenstraße, ehem. Schomaker", minutesAfterPrevious = 2)
    addStation("Nordlohne - Gastw. Brinkmann", minutesAfterPrevious = 2)
    addStation("Stoppelmarkt", minutesAfterPrevious = 11) { isDestination = true }


    addReturnStation {
        name = "Stoppelmarkt"
        thursday {
            "17:45" every 30.Minutes until "02:15"
        }
        friday {
            "14:45" every 30.Minutes until "20:15"
            "20:30" every 10.Minutes until "05:00"
        }
        saturday {
            "13:45" every 20.Minutes until "20:25"
            "20:40" every 10.Minutes until "05:00"
        }
        sunday {
            "13:45" every 30.Minutes until "00:45"
        }
        monday {
            "08:45" every 15.Minutes until "03:00"
        }
        tuesday {
            "14:45" every 30.Minutes until "02:00"
        }
    }
}
