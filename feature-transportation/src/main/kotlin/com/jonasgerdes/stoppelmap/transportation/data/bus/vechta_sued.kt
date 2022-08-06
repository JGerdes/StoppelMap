package com.jonasgerdes.stoppelmap.transportation.data.bus

import com.jonasgerdes.stoppelmap.transportation.data.*

internal fun vechtaSued() = createBusRoute {
    title = "Vechta Süd"
    fixedPrices = prices(adult = 250, children = 180)

    addStation(title = "An der hohen Bank") {
        thursday {
            "18:00" every 60.Minutes until "02:00"
        }
        friday {
            "15:00" every 60.Minutes until "22:00"
            "22:00" every 30.Minutes until "02:00"
        }
        saturday {
            "15:00" every 60.Minutes until "22:00"
            "22:00" every 30.Minutes until "02:00"
        }
        sunday {
            "14:00" every 120.Minutes until "22:00"
        }
        monday {
            "08:00" every 60.Minutes until "02:00"
        }
        tuesday {
            "14:00" every 120.Minutes until "20:00"
        }
    }
    addStation("Tannenweg, Toncoole", minutesAfterPrevious = 1)
    addStation("Evangelischer Friedhof", minutesAfterPrevious = 2)
    addStation("Universität", minutesAfterPrevious = 2)
    addStation("Windallee/Immentum", minutesAfterPrevious = 2)
    addStation("Windallee/Dominikanerweg", minutesAfterPrevious = 3)
    addStation(title = "Stoppelmarkt", minutesAfterPrevious = 6) { isDestination = true }

    addReturnStation {
        title = "Stoppelmarkt"
        thursday {
            "18:40" every 60.Minutes until "02:00"
        }
        friday {
            "15:40" every 60.Minutes until "21:40"
            "21:40" every 30.Minutes until "03:10"
            laterDeparturesOnDemand = true
        }
        saturday {
            "15:40" every 60.Minutes until "21:40"
            "21:40" every 30.Minutes until "03:10"
            laterDeparturesOnDemand = true
        }
        sunday {
            "14:40" every 120.Minutes until "22:40"
        }
        monday {
            "10:40" every 60.Minutes until "02:40"
        }
        tuesday {
            "14:30" every 60.Minutes until "20:30"
            departures("23:00")
        }
    }
}