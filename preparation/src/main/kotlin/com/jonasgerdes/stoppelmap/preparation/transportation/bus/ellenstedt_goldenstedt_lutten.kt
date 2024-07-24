package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.localizedString
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun ellenstedtGoldenstedtLutten() = createBusRoute {
    name = "Ellenstedt - Goldenstedt - Lutten"

    addStation("Goldenstedt, Friedt") {
        prices(400, 200, 6 to 11)
        thursday("15:00", "18:00", "19:00", "20:00", "21:00")
        friday("16:00", "18:00", "19:00", "20:00", "21:00", "22:00")
        saturday("15:00", "19:00", "20:00", "21:00", "22:00")
        monday("09:00", "10:00", "11:00", "12:00", "13:00", "15:00", "17:00", "19:30", "20:30")
        tuesday("15:00", "16:00", "18:00")
    }

    addStation("Goldenstedt, Ambergen") {
        prices(400, 200, 6 to 11)
        thursday("15:03", "18:03", "19:03", "20:03", "21:03")
        friday("16:03", "18:03", "19:03", "20:00", "21:00", "22:00")
        saturday("15:03", "19:03", "20:00", "21:00", "22:00")
        monday("09:00", "10:00", "11:00", "12:03", "13:03", "15:03", "17:03", "19:33", "20:33")
        tuesday("15:03", "16:03", "18:03")
    }
    addStation("Ellenstedt, Kirche") {
        prices(400, 200, 6 to 11)
        friday("20:05", "21:05", "22:05")
        saturday("15:05", "20:05", "21:05", "22:05")
        monday("09:05", "10:05", "11:05", "15:05")
    }
    addStation("Ellenstedt, Hagebuttenstr.") {
        prices(400, 200, 6 to 11)
        friday("20:08", "21:08", "22:08")
        saturday("15:07", "20:08", "21:08", "22:08")
        monday("09:08", "10:08", "11:08", "15:07")
    }

    addStation("Lutten, Zurborg") {
        prices(300, 150, 6 to 11)
        thursday("15:11", "18:11", "19:11", "20:11", "21:11")
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
            "20:41",
        )
        tuesday("15:15", "16:15", "18:15")
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
            "22:20",
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
            "22:20",
        )
    }
    addStation("Lutten, Amerbusch", minutesAfterPrevious = 2) {
        prices(300, 150, 6 to 11)
    }
    addStation("Vechta, Oythe", minutesAfterPrevious = 5) {
        prices(300, 150, 6 to 11)
        monday(
            "09:22",
            "09:27",
            "10:22",
            "10:27",
            "11:22",
            "11:27",
            "12:22",
            "13:22",
            "15:22",
            "17:22",
            "19:40",
            "20:52",
        )
    }

    val returnNotToEllenstedt = localizedString(
        de = "Rückfahrt nicht nach Ellenstedt",
        en = "No return trip to Ellenstedt"
    )
    addReturnStation {
        name = "Stoppelmarkt"
        thursday {
            departure("19:30", annotation = returnNotToEllenstedt)
            departures(
                "20:30", "22:00", "23:00", "00:00", "01:00", "02:00"
            )
        }
        friday {
            departure("18:30", annotation = returnNotToEllenstedt)
            departure("19:30", annotation = returnNotToEllenstedt)
            departure("23:00", annotation = returnNotToEllenstedt)
            departures(
                "20:30",
                "21:30",
                "22:30",
                "00:00",
                "01:00",
                "02:00",
                "03:00",
                "04:00"
            )
        }
        saturday {
            departure("19:30", annotation = returnNotToEllenstedt)
            departure("23:00", annotation = returnNotToEllenstedt)
            departures(
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
        }
        monday {
            departure("11:30", annotation = returnNotToEllenstedt)
            departure("20:00", annotation = returnNotToEllenstedt)
            departure("22:00", annotation = returnNotToEllenstedt)
            departures(
                "14:00",
                "16:00",
                "18:00",
                "19:00",
                "21:00",
                "23:00",
                "00:00"
            )
        }
        tuesday("17:30", "18:30", "22:30")
    }
}
