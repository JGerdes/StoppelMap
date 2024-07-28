package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun vechtaStadt() = createBusRoute {
    name = "Vechta Stadt"
    operatorSlug = TransportOperatorSlugs.wilmering
    fixedPrices = prices(adult = 250, children = 180)

    addStation("Sgundek") {
        thursday {
            "18:00" every 30.Minutes until "02:30"
        }
        friday {
            "15:00" every 30.Minutes until "21:00"
            "21:00" every 20.Minutes until "03:40"
        }
        saturday {
            "15:00" every 30.Minutes until "21:00"
            "21:00" every 20.Minutes until "03:40"
        }
        sunday {
            "13:00" every 60.Minutes until "00:00"
        }
        monday {
            "08:00" every 30.Minutes until "11:00"
            "11:00" every 20.Minutes until "15:00"
            "15:00" every 30.Minutes until "01:30"
        }
        tuesday {
            "14:00" every 60.Minutes until "20:00"
        }
    }
    addStation("Am Schützenplatz", minutesAfterPrevious = 1)
    addStation("Münsterstraße, Wessel", minutesAfterPrevious = 2)
    addStation("Münsterstraße/Hagener Straße", minutesAfterPrevious = 2)
    addStation("Burgstraße, Altes Finanzamt", minutesAfterPrevious = 3)
    addStation("Klingenhagen", minutesAfterPrevious = 1)
    addStation("Füchteler Straße/Krusenschlopp", minutesAfterPrevious = 1)
    addStation("Botenkamp/Georgstraße", minutesAfterPrevious = 2)
    addStation("Botenkamp/Markusstraße", minutesAfterPrevious = 1)
    addStation("Stoppelmarkt", minutesAfterPrevious = 2) { isDestination = true }

    addReturnStation {
        name = "Stoppelmarkt"
        thursday {
            "18:15" every 30.Minutes until "02:45"
        }
        friday {
            "15:15" every 30.Minutes until "20:45"
            "21:00" every 20.Minutes until "04:00"
            laterDeparturesOnDemand = true
        }
        saturday {
            "15:15" every 30.Minutes until "20:45"
            "21:00" every 20.Minutes until "04:00"
            laterDeparturesOnDemand = true
        }
        sunday {
            "13:30" every 60.Minutes until "00:30"
        }
        monday {
            departures("10:15", "10:45")
            "11:20" every 20.Minutes until "15:00"
            "15:15" every 30.Minutes until "01:45"
        }
        tuesday {
            "14:30" every 60.Minutes until "20:30"
            departures("23:00")
        }
    }
}
