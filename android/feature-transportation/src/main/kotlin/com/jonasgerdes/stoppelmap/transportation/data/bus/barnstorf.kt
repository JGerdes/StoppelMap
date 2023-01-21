package com.jonasgerdes.stoppelmap.transportation.data.bus

import com.jonasgerdes.stoppelmap.transportation.data.addReturnStation
import com.jonasgerdes.stoppelmap.transportation.data.addStation
import com.jonasgerdes.stoppelmap.transportation.data.createBusRoute
import com.jonasgerdes.stoppelmap.transportation.data.prices

internal fun barnstorf() = createBusRoute {
    title = "Barnstorf"

    addStation("Barnstorf, Markt") {
        prices(500, 250, childrenAgeRange = 6 to 11)

        friday("20:00", "20:30", "21:00", "21:30")
        saturday("16:30", "20:00", "20:30", "21:00", "21:15", "21:30")
        monday("09:30", "10:30", "11:30", "18:30")
    }
    addStation("Barnstorf, Walsen", minutesAfterPrevious = 3) {
        prices(500, 250, childrenAgeRange = 6 to 11)
    }
    addStation("Rödenbeck", minutesAfterPrevious = 2) {
        prices(500, 250, childrenAgeRange = 6 to 11)
    }
    addStation("Lahrheide", minutesAfterPrevious = 3) {
        prices(400, 200, childrenAgeRange = 6 to 11)
    }
    addStation("Varenesch", minutesAfterPrevious = 1) {
        prices(400, 200, childrenAgeRange = 6 to 11)
    }
    addStation("Goldenstedt, Heide", minutesAfterPrevious = 1) {
        prices(400, 200, childrenAgeRange = 6 to 11)
    }
    addStation("Lutten, Kirche") {
        prices(300, 150, childrenAgeRange = 6 to 11)
        friday("20:45", "21:45")
        saturday("16:45", "20:45", "21:30", "21:45")
        monday("09:45", "10:45", "11:45", "18:45")
    }
    addStation("Stoppelmarkt") {
        isDestination = true
    }

    addReturnStation {
        title = "Stoppelmarkt"
        friday("20:30", "21:00", "01:00", "02:00")
        saturday("20:30", "21:00", "01:00", "02:00", "03:00")
        monday("18:00", "20:00", "22:00", "00:00")
    }
}
