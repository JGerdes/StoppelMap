package com.jonasgerdes.stoppelmap.transportation.data.bus

import com.jonasgerdes.stoppelmap.transportation.data.addReturnStation
import com.jonasgerdes.stoppelmap.transportation.data.addStation
import com.jonasgerdes.stoppelmap.transportation.data.createBusRoute
import com.jonasgerdes.stoppelmap.transportation.data.prices

internal fun calveslage() = createBusRoute {
    title = "Calveslage"

    addStation("Lohe, Möller") {
        prices(330, 240)

        thursday("19:10")
        friday("19:10", "20:40")
        saturday("19:10", "20:40")
        monday("10:45", "15:45")
    }
    addStation("Lohe, Hölscher", minutesAfterPrevious = 3) { prices(330, 240) }
    addStation("Lohe, König", minutesAfterPrevious = 1) { prices(330, 240) }
    addStation("Lohe, Baumann", minutesAfterPrevious = 2) { prices(330, 240) }
    addStation("Calveslage, Rispenweg", minutesAfterPrevious = 9) { prices(280, 200) }
    addStation("Calveslage, Wiesenweg", minutesAfterPrevious = 2) { prices(280, 200) }
    addStation("Calveslage, Bakumer Str.", minutesAfterPrevious = 2) { prices(280, 200) }
    addStation("Stoppelmarkt (»HofGisela«)", minutesAfterPrevious = 10) { isDestination = true }


    addReturnStation {
        title = "Stoppelmarkt"
        thursday("23:00")
        friday("00:00")
        saturday("00:00")
        monday("21:00")
    }
}
