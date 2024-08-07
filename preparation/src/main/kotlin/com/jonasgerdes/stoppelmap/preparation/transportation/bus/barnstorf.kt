package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.vnn
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun barnstorf() = createBusRoute {
    name = "Barnstorf"
    operatorSlug = vnn
    arrivalStationSlug = busbahnhofOst

    returns {
        friday("20:30", "21:00", "01:00", "02:00")
        saturday("20:30", "21:00", "01:00", "02:00", "03:00")
        monday("18:00", "20:00", "22:00")
    }

    addStation("Barnstorf, Markt") {
        prices(500, 250, childrenAgeRange = 6 to 11)

        outward {
            friday("20:00", "20:30", "21:00", "21:30")
            saturday("16:30", "20:00", "20:30", "21:00", "21:30")
            monday("10:30", "11:30", "18:30")
        }
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
    addStation("Goldenstedt, Heide", minutesAfterPrevious = 3) {
        prices(400, 200, childrenAgeRange = 6 to 11)
    }
    addStation("Lutten, Kirche") {
        prices(300, 150, childrenAgeRange = 6 to 11)
        outward {
            friday("20:45", "21:45")
            saturday("16:45", "20:45", "21:45")
            monday("10:45", "11:45", "18:45")
        }
    }
}
