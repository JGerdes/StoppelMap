package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun ellenstedtGoldenstedtLutten() = createBusRoute {
    title = "Ellenstedt - Goldenstedt - Lutten"

    addStation("Goldenstedt, Ambergen") {
        prices(400, 200, 6 to 11)
        friday("20:00", "21:00", "22:00")
        saturday("14:55", "20:00", "21:00", "22:00")
        monday("09:00", "10:00", "11:00", "14:55", "19:22", "21:22")
    }
    addStation("Ellenstedt, Kirche", minutesAfterPrevious = 5) {
        prices(400, 200, 6 to 11)
    }
    addStation("Ellenstedt, Hagebuttenstr.", minutesAfterPrevious = 3) {
        prices(400, 200, 6 to 11)
    }
    addStation("Goldenstedt, Hagebuttenstr.", minutesAfterPrevious = 3) {
        prices(400, 200, 6 to 11)
    }
    addStation("Goldenstedt, Friedt") {
        prices(400, 200, 6 to 11)
        thursday("15:00", "18:00", "19:00", "20:00", "21:00")
        friday("16:00", "18:00", "19:00", "20:00", "21:00", "22:00")
        saturday("15:00", "19:00", "20:00", "21:00", "22:00")
        monday("09:00", "10:00", "11:00", "12:00", "13:00", "15:00", "17:00", "20:30")
        tuesday("15:00", "16:00", "18:00")
    }
    addStation("Goldenstedt, Heide", minutesAfterPrevious = 7) {
        prices(400, 200, 6 to 11)
        saturday("15:07", "16:42", "19:07", "20:07", "21:07", "22:07")
    }
    addStation("Goldenstedt, Heider-Schule", minutesAfterPrevious = 2) {
        prices(400, 200, 6 to 11)
    }
    addStation("Lutten, Zurborg", minutesAfterPrevious = 2) {
        prices(300, 150, 6 to 11)
        friday("16:11", "18:11", "19:11", "20:11", "20:16", "21:11", "21:16", "22:11", "22:16")
        saturday("15:11", "16:46", "19:11", "20:11", "20:16", "21:11", "21:16", "22:11", "22:16")
        monday(
            "09:11",
            "09:15",
            "10:11",
            "10:15",
            "11:11",
            "11:15",
            "12:11",
            "13:11",
            "15:11",
            "17:11",
            "19:41",
            "20:41",
            "21:41"
        )
    }
    addStation("Lutten, Kirche", minutesAfterPrevious = 4) {
        prices(300, 150, 6 to 11)
        friday(
            "16:15",
            "18:15",
            "19:15",
            "20:15",
            "20:20",
            "21:15",
            "21:20",
            "22:05",
            "22:15",
            "22:20"
        )
        saturday(
            "15:15",
            "16:50",
            "19:15",
            "20:15",
            "20:20",
            "21:15",
            "21:20",
            "22:05",
            "22:15",
            "22:20"
        )
    }
    addStation("Lutten, Amerbusch", minutesAfterPrevious = 2) {
        prices(300, 150, 6 to 11)
    }
    addStation("Vechta, Oythe", minutesAfterPrevious = 5) {
        prices(300, 150, 6 to 11)
        saturday(
            "15:22",
            "16:54",
            "19:22",
            "20:22",
            "20:27",
            "21:22",
            "21:27",
            "22:12",
            "22:22",
            "22:27"
        )
    }

    addReturnStation {
        title = "Stoppelmarkt (ohne Halt in Ellenstedt)"
        thursday("19:30", "20:30", "22:00", "23:00", "00:00", "01:00", "02:00")
        friday(
            "18:30",
            "19:30",
            "20:30",
            "21:30",
            "22:30",
            "23:00",
            "00:00",
            "01:00",
            "02:00",
            "03:00",
            "04:00"
        )
        saturday(
            "17:30",
            "19:30",
            "20:30",
            "21:30",
            "22:30",
            "23:00",
            "00:00",
            "01:00",
            "02:00",
            "03:00",
            "03:30",
            "04:00"
        )
        monday(
            "11:30",
            "14:00",
            "16:00",
            "18:00",
            "19:00",
            "20:00",
            "21:00",
            "22:00",
            "23:00",
            "00:00"
        )
        tuesday("17:30", "18:30", "22:30")
    }

    addReturnStation {
        title = "Stoppelmarkt (inkl Halt in Ellenstedt)"
        thursday("20:30", "22:00", "23:00", "00:00", "01:00", "02:00")
        friday(
            "20:30",
            "21:30",
            "22:30",
            "00:00",
            "01:00",
            "02:00",
            "03:00",
            "04:00"
        )
        saturday(
            "17:30",
            "20:30",
            "21:30",
            "22:30",
            "00:00",
            "01:00",
            "02:00",
            "03:00",
            "03:30",
            "04:00"
        )
        monday(
            "14:00",
            "16:00",
            "18:00",
            "19:00",
            "21:00",
            "23:00",
            "00:00"
        )
        tuesday("17:30", "18:30", "22:30")
    }
}
