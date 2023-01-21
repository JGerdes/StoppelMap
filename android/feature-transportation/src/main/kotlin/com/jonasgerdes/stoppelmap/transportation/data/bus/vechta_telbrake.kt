package com.jonasgerdes.stoppelmap.transportation.data.bus

import com.jonasgerdes.stoppelmap.transportation.data.*

internal fun vechtaTelbrake() = createBusRoute {
    title = "Vechta Telbrake"
    fixedPrices = prices(adult = 250, children = 180)

    addStation("Telbrake, Fragge") {
        thursday {
            "18:26" every 120.Minutes until "00:26"
        }
        friday {
            "15:26" every 120.Minutes until "01:26"
        }
        saturday {
            "15:26" every 120.Minutes until "01:26"
        }
        sunday {
            "15:26" every 120.Minutes until "21:26"
        }
        monday {
            "08:26" every 120.Minutes until "00:26"
        }
        tuesday {
            "15:26" every 120.Minutes until "21:26"
        }
    }
    addStation("Telbrake, Langer Damm", minutesAfterPrevious = 1)
    addStation("Telbrake, Sträpel", minutesAfterPrevious = 2)
    addStation(
        "Telbrake, Telbraker Straße/Graf-von-Stauffenberg-Straße",
        minutesAfterPrevious = 1
    ) { isNew = true }
    addStation("Telbrake, Hoher Esch", minutesAfterPrevious = 1)
    addStation("Oythe, Treffpunkt", minutesAfterPrevious = 1)
    addStation("Stoppelmarkt", minutesAfterPrevious = 4) { isDestination = true }

    addReturnStation {
        title = "Stoppelmarkt"
        thursday {
            "20:20" every 120.Minutes until "00:20"
        }
        friday {
            "17:20" every 120.Minutes until "01:20"
        }
        saturday {
            "17:20" every 120.Minutes until "01:20"
        }
        sunday {
            "17:20" every 120.Minutes until "21:20"
        }
        monday {
            "10:20" every 120.Minutes until "00:20"
        }
        tuesday {
            "17:20" every 120.Minutes until "19:20"
        }
    }
}
