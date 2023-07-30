package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun visbek() = createBusRoute {
    title = "Visbek"

    addStation("Hagstedt, Mittlere Siedlungstraße") {
        prices(460, 280)

        friday("19:50", "21:00", "22:00", "23:00")
        saturday("12:44", "15:14", "16:44", "18:50", "19:50", "21:00", "22:00", "23:00")
        monday("08:50", "10:50", "13:50", "15:50")
    }
    addStation("Hagstedt, Hagstedt", minutesAfterPrevious = 1) { prices(460, 280) }
    addStation("Erlte, Abzw. Meyerhöfen", minutesAfterPrevious = 1) { prices(460, 280) }
    addStation("Erlte, Hogeback", minutesAfterPrevious = 3) { prices(460, 280) }
    addStation("Visbek, Elektro Schulz") {
        prices(460, 280)

        friday("19:57", "20:57", "21:57", "22:57")
        saturday("12:35", "15:23", "16:35", "18:57", "19:57", "20:57", "21:57", "22:57")
        monday("08:57", "10:57", "13:57", "15:57")
    }
    addStation("Visbek, Markt", minutesAfterPrevious = 3) { prices(460, 280) }
    addStation("Visbek, Zur Tränke", minutesAfterPrevious = 3) { prices(460, 280) }
    addStation("Astrup, Witte") {
        prices(460, 280)

        friday("20:06", "21:08", "22:08", "23:08")
        saturday("19:06", "20:06", "21:08", "22:08", "23:08")
        monday("09:06", "11:06", "14:06", "16:06")
    }
    addStation("Astrup, Lübberding") {
        prices(460, 280)

        friday("20:10", "21:10", "22:10", "23:10")
        saturday("12:11", "15:47", "16:11", "19:10", "20:10", "21:10", "22:10", "23:10")
        monday("09:10", "11:10", "14:10", "16:10")
    }
    addStation("Norddöllen, Frilling", minutesAfterPrevious = 2) { prices(430, 260) }
    addStation("Norddöllen, Wempe", minutesAfterPrevious = 3) { prices(430, 260) }
    addStation("Wöstendöllen") {
        prices(430, 260)
        saturday("12:18", "15:40", "16:18")
    }
    addStation("Stoppelmarkt") {
        isDestination = true
        friday("20:23", "21:20", "22:20", "23:20")
        saturday("12:50", "15:54", "16:50", "19:23", "20:23", "21:20", "22:20", "23:20")
        sunday("09:23", "11:23", "14:23", "16:23")
    }

    addReturnStation {
        title = "Stoppelmarkt (»Hof Giesela«)"
        friday("22:20", "00:30", "02:00", "03:00", "04:30")
        saturday("22:20", "00:30", "02:00", "03:00", "04:30")
        sunday("13:10", "15:10", "20:30", "21:30")
    }
    addReturnStation {
        title = "Stoppelmarkt (Visbeker Damm)"
        saturday("16:04")
    }
    addReturnStation {
        title = "Stoppelmarkt (Oldenburger Str.)"
        saturday("15:04")
    }
}
