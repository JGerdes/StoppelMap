package com.jonasgerdes.stoppelmap.transportation.data.bus

import com.jonasgerdes.stoppelmap.transportation.data.*

internal fun lohne_stadt() = createBusRoute {
    title = "Lohne Stadt"
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
        title = "Stoppelmarkt"
        thursday {
            "17:40" every 30.Minutes until "02:10"
        }
        friday {
            "14:40" every 30.Minutes until "20:10"
            "20:30" every 10.Minutes until "05:00"
        }
        saturday {
            "13:40" every 20.Minutes until "20:30"
            "20:30" every 10.Minutes until "05:00"
        }
        sunday {
            "13:30" every 30.Minutes until "14:00"
        }
        monday {
            "08:40" every 15.Minutes until "03:00"
        }
        tuesday {
            "14:40" every 30.Minutes until "02:00"
        }
    }
}
