package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun diepholz() = createBusRoute {
    title = "Diepholz"
    additionalInfo = "Die Haltstelle Diepholz-Krankenhaus kann aufgrund einer Baustelle " +
            "in diesem Jahr leider nicht bedient werden. " +
            "Bitte nutzen Sie alternativ die Haltestellen Diepholz-Bahnhof " +
            "oder Diepholz-Marktplatz."

    addStation("Diepholz, Schulzentrum") {
        prices(500, 370)

        friday("19:00", "20:00", "21:00", "22:00", "23:00")
        saturday("19:00", "20:00", "21:00", "22:00", "23:00")
        monday("08:30", "10:00", "13:00", "15:00", "18:00", "20:00")
    }
    addStation("Diepholz, Abzweig Moorstraße", minutesAfterPrevious = 2) { prices(500, 370) }
    addStation("Diepholz, K+K Markt, Lüderstraße", minutesAfterPrevious = 1) { prices(500, 370) }
    addStation("Diepholz, Freibad", minutesAfterPrevious = 1) { prices(500, 370) }
    addStation("Diepholz, katholische Kirche", minutesAfterPrevious = 2) { prices(500, 370) }
    addStation("Diepholz, Bahnhof", minutesAfterPrevious = 2) { prices(500, 370) }
    addStation("Diepholz, Marktplatz", minutesAfterPrevious = 4) { prices(500, 370) }
    addStation("Diepholz, Diemastraße") {
        prices(500, 370)
        friday("23:14")
        saturday("23:14")
        monday("08:44", "10:14", "13:14", "18:14")
    }
    addStation("St. Hülfe, B 51") {
        prices(480, 350)
        friday("20:00", "21:00", "22:00", "23:16")
        saturday("20:00", "21:00", "22:00", "23:16")
        monday("08:46", "10:16", "13:16", "18:16")
    }
    addStation("Heede, Ripking", minutesAfterPrevious = 4) { prices(480, 350) }
    addStation("Heede, Liegertsiedlung", minutesAfterPrevious = 1) { prices(480, 350) }
    addStation("Jacobidrebber, Friedhof", minutesAfterPrevious = 2) { prices(480, 350) }
    addStation("Mariendrebber, Halfbrodt", minutesAfterPrevious = 2) { prices(480, 350) }
    addStation("Aschen, Milbe", minutesAfterPrevious = 5) { prices(480, 350) }
    addStation("Schobrink, Paradiek", minutesAfterPrevious = 6) { prices(350, 260) }
    addStation("Stoppelmarkt") {
        isDestination = true
        friday("19:35", "20:35", "20:35", "21:35", "21:35", "22:35", "22:35", "23:50")
        saturday("19:35", "20:35", "20:35", "21:35", "21:35", "22:35", "22:35", "23:50")
        monday("09:20", "10:50", "13:50", "15:35", "18:50", "20:35")
    }

    addReturnStation {
        title = "Stoppelmarkt"
        friday("23:30", "01:00", "02:30", "04:00")
        saturday("23:30", "01:00", "02:30", "04:00")
        monday {
            departures("12:00", "14:00", "17:00", "19:00", "00:00")
            departure("22:30", annotation = "nur nach Diepholz")
        }
    }
}
