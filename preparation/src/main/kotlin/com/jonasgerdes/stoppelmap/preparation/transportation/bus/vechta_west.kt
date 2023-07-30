package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun vechtaWest() = createBusRoute {
    title = "Vechta West"
    fixedPrices = prices(adult = 250, children = 180)

    addStation("Dersastraße, Lohner Straße") {
        thursday {
            "18:30" every 30.Minutes until "02:30"
        }
        friday {
            "15:00" every 60.Minutes until "18:00"
            "18:00" every 30.Minutes until "03:30"
        }
        saturday {
            "15:00" every 60.Minutes until "18:00"
            "18:00" every 30.Minutes until "03:30"
        }
        sunday {
            "13:00" every 60.Minutes until "00:00"
        }
        monday {
            "08:00" every 30.Minutes until "02:30"
        }
        tuesday {
            "14:00" every 60.Minutes until "20:00"
        }
    }
    addStation("Dersastraße/Gerbertstraße", minutesAfterPrevious = 2)
    addStation("Achtern Diek", minutesAfterPrevious = 2)
    addStation("Rombergstraße, Finanzamt", minutesAfterPrevious = 2)
    addStation("Theodor-Heuß-Straße, Spielplatz", minutesAfterPrevious = 2)
    addStation("Theodor-Heuß-Straße/Stresemannstraße", minutesAfterPrevious = 2)
    addStation("Paulus-Bastei/Theodor-Heuß-Straße", minutesAfterPrevious = 2)
    addStation("Theodor-Heuß-Straße/H.-Wilhelm-Kopf-Str.", minutesAfterPrevious = 1)
    addStation("Falkenrotter Straße, Mehl Kröger", minutesAfterPrevious = 1)
    addStation("St.-Florian-Straße", minutesAfterPrevious = 1)
    addStation("Stoppelmarkt", minutesAfterPrevious = 5) { isDestination = true }

    addReturnStation {
        title = "Stoppelmarkt"
        thursday {
            "19:00" every 30.Minutes until "03:00"
        }
        friday {
            "15:30" every 60.Minutes until "18:30"
            "18:30" every 30.Minutes until "04:00"
            laterDeparturesOnDemand = true
        }
        saturday {
            "15:30" every 60.Minutes until "18:30"
            "18:30" every 30.Minutes until "04:00"
            laterDeparturesOnDemand = true
        }
        sunday {
            "14:30" every 60.Minutes until "00:30"
        }
        monday {
            "10:30" every 30.Minutes until "03:00"
        }
        tuesday {
            "14:30" every 60.Minutes until "20:30"
            departures("23:00")
        }
    }
}
