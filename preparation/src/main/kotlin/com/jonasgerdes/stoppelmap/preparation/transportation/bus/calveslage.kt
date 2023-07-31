package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun calveslage() = createBusRoute {
    title = "Calveslage"
    additionalInfo =
        "Aufgrund der Bauarbeiten ist die Linienführung in Lohe in diesem Jahr anders als gewohnt."

    addStation("Lohe, Hölscher") {
        prices(350, 220)

        thursday("19:10")
        friday("19:10", "20:40")
        saturday("19:10", "20:40")
        monday("10:45", "15:45")
    }
    addStation("Lohe, Goseborg", minutesAfterPrevious = 2) { prices(350, 220) }
    addStation("Lohe, Möller", minutesAfterPrevious = 2) { prices(350, 220) }
    addStation("Calveslage, Rispenweg", minutesAfterPrevious = 5) { prices(280, 200) }
    addStation("Calveslage, Wiesenweg", minutesAfterPrevious = 2) { prices(280, 200) }
    addStation("Calveslage, Bakumer Str.", minutesAfterPrevious = 2) { prices(280, 200) }
    addStation("Stoppelmarkt („Hof Gisela“)", minutesAfterPrevious = 10) { isDestination = true }


    addReturnStation {
        title = "Stoppelmarkt („Hof Gisela“)"
        thursday("23:00")
        friday("00:00")
        saturday("00:00")
        monday("21:00")
    }
}
