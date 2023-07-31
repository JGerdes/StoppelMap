package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun vechtaTelbrake() = createBusRoute {
    title = "Vechta Telbrake"
    fixedPrices = prices(adult = 250, children = 180)

    addStation("Telbrake, Fragge") {
        thursday {
            "18:26" every 60.Minutes until "02:26"
        }
        friday {
            "15:26" every 60.Minutes until "02:26"
        }
        saturday {
            "15:26" every 60.Minutes until "02:26"
        }
        sunday {
            "15:26" every 60.Minutes until "22:26"
        }
        monday {
            "08:26" every 60.Minutes until "02:26"
        }
        tuesday {
            "15:26" every 60.Minutes until "20:26"
        }
    }
    addStation("Telbrake, Langer Damm", minutesAfterPrevious = 1)
    addStation("Telbrake, Sträpel", minutesAfterPrevious = 2)
    addStation(
        "Telbrake, Telbraker Straße/Graf-von-Stauffenberg-Straße",
        minutesAfterPrevious = 1
    )
    addStation("Telbrake, Hoher Esch", minutesAfterPrevious = 1)
    addStation("Oythe, Treffpunkt", minutesAfterPrevious = 1)
    addStation("Stoppelmarkt", minutesAfterPrevious = 4) { isDestination = true }

    addReturnStation {
        title = "Stoppelmarkt"
        thursday {
            "18:20" every 60.Minutes until "02:20"
        }
        friday {
            "15:20" every 60.Minutes until "02:20"
        }
        saturday {
            "15:20" every 60.Minutes until "02:20"
        }
        sunday {
            "15:20" every 60.Minutes until "22:20"
        }
        monday {
            "10:20" every 60.Minutes until "02:20"
        }
        tuesday {
            "17:20" every 60.Minutes until "20:20"
            departure("22:30")
        }
    }
}
