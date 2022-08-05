package com.jonasgerdes.stoppelmap.transportation.data.bus

import com.jonasgerdes.stoppelmap.transportation.data.addReturnStation
import com.jonasgerdes.stoppelmap.transportation.data.addStation
import com.jonasgerdes.stoppelmap.transportation.data.createBusRoute
import com.jonasgerdes.stoppelmap.transportation.data.prices

internal fun bakumEssen() = createBusRoute {
    title = "Bakum und Essen"

    addStation("Essen, Bahnhof") {
        prices(570, 420)
        saturday("14:16")
    }
    addStation("Essen, Markt", minutesAfterPrevious = 5) { prices(570, 420) }
    addStation("Uptloh, Tellmann", minutesAfterPrevious = 3) { prices(570, 420) }
    addStation("Bevern, Feuerwehr", minutesAfterPrevious = 1) { prices(530, 370) }
    addStation("Addrup, Wernsing", minutesAfterPrevious = 2) { prices(530, 370) }
    addStation("Lüsche, Büter", minutesAfterPrevious = 2) { prices(530, 370) }
    addStation("Carum, Ort") {
        prices(480, 310)
        thursday("19:30")
        friday("18:00", "19:30")
        saturday("18:00", "19:30")
        sunday("15:00", "18:00")
        monday("08:15", "10:00", "14:00", "18:00")
        tuesday("18:00")
    }
    addStation("Lüsche, Post") {
        prices(510, 340)
        thursday("19:40")
        friday("18:10", "19:40")
        saturday("14:34", "18:10", "19:40")
        sunday("15:10", "18:10")
        monday("08:25", "10:10", "14:10", "18:10")
        tuesday("18:10")
    }
    addStation("Lüsche, Abeln", minutesAfterPrevious = 1) { prices(510, 340) }
    addStation("Hausstette, Kreuz", minutesAfterPrevious = 2) { prices(370, 270) }
    addStation("Hausstette, Bührmann", minutesAfterPrevious = 2) { prices(370, 270) }
    addStation("Hausstette, Tiemerding", minutesAfterPrevious = 5) { prices(370, 270) }
    addStation("Hausstette, Schule", minutesAfterPrevious = 1) { prices(370, 270) }
    addStation("Hausstette, Koops Mühle", minutesAfterPrevious = 1) { prices(370, 270) }
    addStation("Vestrup, Kellermann", minutesAfterPrevious = 2) { prices(370, 270) }
    addStation("Vestrup, Kirche") {
        prices(370, 270)
        thursday("19:55")
        friday("18:25", "19:55")
        saturday("13:00", "14:43", "18:25", "19:55")
        sunday("15:25", "18:25")
        monday("08:40", "10:25", "12:25", "14:25", "18:25")
        tuesday("18:25")
    }
    addStation("Vestrup, Bahnhof", minutesAfterPrevious = 1) { prices(370, 270) }
    addStation("Westerbakum, Viehverwertung", minutesAfterPrevious = 1) { prices(350, 220) }
    addStation("Bakum,Rathaus") {
        prices(350, 220)
        thursday("20:00")
        friday("18:30", "20:00", "21:00", "22:00", "23:00")
        saturday("13:05", "14:47", "18:30", "20:00", "21:00", "22:00", "23:00")
        sunday("15:30", "18:30")
        monday("08:45", "10:30", "12:30", "14:30", "18:30")
        tuesday("18:30")
    }
    addStation("Bakum,Kirchesch", minutesAfterPrevious = 2) { prices(350, 220) }
    addStation("Elmelage", minutesAfterPrevious = 1) { prices(350, 220) }
    addStation("Schledehausen, Kreuz", minutesAfterPrevious = 2) { prices(300, 180) }
    addStation("Schledehausen, Büssing", minutesAfterPrevious = 3) { prices(300, 180) }
    addStation("Schledehausen, Többen", minutesAfterPrevious = 2) { prices(300, 180) }
    addStation("Stoppelmarkt") {
        isDestination = true
        thursday("20:20")
        friday("18:50", "20:20", "21:20", "22:20", "23:20")
        saturday("13:25", "15:04", "18:50", "20:20", "21:20", "22:20", "23:20")
        sunday("15:50", "18:50")
        monday("09:05", "10:50", "12:50", "14:50", "18:50")
        tuesday("18:50")
    }

    addReturnStation {
        title = "Stoppelmarkt bis Bakum"
        friday("20:30", "21:30", "22:30")
        saturday("20:30", "21:30", "22:30")
    }
    addReturnStation {
        title = "Stoppelmarkt bis Vestrup (über Bakum)"
        thursday("23:00")
    }
    addReturnStation {
        title = "Stoppelmarkt bis Carum (über Bakum und Vestrup)"
        thursday("01:00")
        friday("19:00", "00:30", "01:30", "02:30", "03:30", "04:30")
        saturday("19:00", "00:30", "01:30", "02:30", "03:30", "04:30")
        sunday("19:00", "23:00")
        monday("13:00", "17:00", "20:30", "22:30", "00:30", "02:30")
        tuesday("09:00", "22:30")
    }
}
