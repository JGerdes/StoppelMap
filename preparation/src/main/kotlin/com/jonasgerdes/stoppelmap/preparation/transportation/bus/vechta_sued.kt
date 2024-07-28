package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun vechtaSued() = createBusRoute {
    name = "Vechta Süd"
    operatorSlug = TransportOperatorSlugs.wilmering
    fixedPrices = prices(adult = 250, children = 180)

    addStation(name = "An der hohen Bank") {
        thursday {
            "18:00" every 60.Minutes until "02:00"
        }
        friday {
            "15:00" every 60.Minutes until "02:00"
        }
        saturday {
            "15:00" every 60.Minutes until "02:00"
        }
        sunday {
            "15:00" every 60.Minutes until "22:00"
        }
        monday {
            "08:00" every 60.Minutes until "02:00"
        }
        tuesday {
            "15:00" every 60.Minutes until "20:00"
        }
    }
    addStation("Tannenweg, Toncoole", minutesAfterPrevious = 1)
    addStation("Evangelischer Friedhof", minutesAfterPrevious = 2)
    addStation("Universität", minutesAfterPrevious = 2)
    addStation("Windallee/Immentum", minutesAfterPrevious = 2)
    addStation("Windallee/Dominikanerweg", minutesAfterPrevious = 3)
    addStation(name = "Stoppelmarkt", minutesAfterPrevious = 6) { isDestination = true }

    addReturnStation {
        name = "Stoppelmarkt"
        thursday {
            "18:40" every 60.Minutes until "02:40"
        }
        friday {
            "15:40" every 60.Minutes until "02:40"
        }
        saturday {
            "15:40" every 60.Minutes until "02:40"
        }
        sunday {
            "15:40" every 60.Minutes until "22:40"
        }
        monday {
            "10:40" every 60.Minutes until "02:40"
        }
        tuesday {
            "15:40" every 60.Minutes until "20:40"
            departures("23:00")
        }
    }
}
