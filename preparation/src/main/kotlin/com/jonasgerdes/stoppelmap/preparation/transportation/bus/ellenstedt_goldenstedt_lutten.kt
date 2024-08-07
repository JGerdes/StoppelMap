package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.StationScope
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.vnn
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

private fun StationScope.ellenstedtReturns() = returns {
    thursday("20:30", "22:00", "23:00", "00:00", "01:00", "02:00")
    friday("20:30", "21:30", "22:30", "00:00", "01:00", "02:00", "03:00", "04:00")
    saturday("17:30", "20:30", "21:30", "22:30", "00:00", "01:00", "02:00", "03:00", "04:00")
    monday("14:00", "16:00", "18:00", "19:00", "21:00", "23:00", "00:00")
    tuesday("17:30", "18:30", "22:30")
}

internal fun ellenstedtGoldenstedtLutten() = createBusRoute {
    name = "Ellenstedt - Goldenstedt - Lutten"
    operatorSlug = vnn
    arrivalStationSlug = busbahnhofOst

    returns {
        thursday("19:30", "20:30", "22:00", "23:00", "00:00", "01:00", "02:00")
        friday("18:30", "19:30", "20:30", "21:30", "22:30", "23:00", "00:00", "01:00", "02:00", "03:00", "04:00")
        saturday("17:30", "19:30", "20:30", "21:30", "22:30", "23:00", "00:00", "01:00", "02:00", "03:00", "04:00")
        monday("11:30", "14:00", "16:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00")
        tuesday("17:30", "18:30", "22:30")
    }

    addStation("Goldenstedt, Friedt") {
        prices(400, 200, 6 to 11)
        outward {
            thursday("18:00", "19:00", "20:00", "21:00")
            friday("16:00", "18:00", "19:00", "20:00", "21:00", "22:00")
            saturday("15:00", "19:00", "20:00", "21:00", "22:00")
            monday("09:00", "10:00", "11:00", "12:00", "13:00", "15:00", "17:00", "19:30", "20:30")
            tuesday("15:00", "16:00", "18:00")
        }
    }
    addStation("Goldenstedt, Heide", minutesAfterPrevious = 7) {
        prices(400, 200, 6 to 11)
    }

    addStation("Goldenstedt, Heider-Schule", minutesAfterPrevious = 2) {
        prices(400, 200, 6 to 11)
    }

    addStation("Goldenstedt, Ambergen") {
        prices(400, 200, 6 to 11)
        outward {
            friday("20:00", "21:00", "22:00")
            saturday("15:03", "20:00", "21:00", "22:00")
            monday("09:00", "10:00", "11:00")
        }
    }
    addStation("Ellenstedt, Kirche") {
        prices(400, 200, 6 to 11)
        outward {
            friday("20:05", "21:05", "22:05")
            saturday("15:05", "20:05", "21:05", "22:05")
            monday("09:05", "10:05", "11:05")
        }

        ellenstedtReturns()
    }
    addStation("Ellenstedt, Hagebuttenstr.") {
        prices(400, 200, 6 to 11)
        outward {
            friday("20:08", "21:08", "22:08")
            saturday("15:05", "20:08", "21:08", "22:05")
            monday("09:08", "10:08", "11:08")
        }
        ellenstedtReturns()
    }

    addStation("Lutten, Zurborg") {
        prices(300, 150, 6 to 11)
        outward {
            thursday("18:11", "19:11", "20:11", "21:11")
            friday("16:11", "18:11", "19:11", "20:11", "20:16", "21:11", "21:16", "22:11", "22:16")
            saturday("15:16", "19:11", "20:11", "20:16", "21:11", "21:16", "22:11", "22:16")
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
    }
    addStation("Lutten, Kirche") {
        prices(300, 150, 6 to 11)
        outward {
            thursday(
                "18:15",
                "19:15",
                "20:15",
                "20:20",
                "21:15",
            )
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
                "15:20",
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
    }
    addStation("Lutten, Amerbusch", minutesAfterPrevious = 2) {
        prices(300, 150, 6 to 11)
    }
    addStation("Vechta, Oythe", minutesAfterPrevious = 5) {
        prices(300, 150, 6 to 11)
        outward {
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
    }
}
