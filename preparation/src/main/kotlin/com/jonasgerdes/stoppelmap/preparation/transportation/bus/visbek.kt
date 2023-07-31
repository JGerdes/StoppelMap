package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun visbek() = createBusRoute {
    title = "Visbek"
    fixedPrices = prices(adult = 480, children = 290)

    addStation("Hagstedt, Mittlere Siedlungstraße") {
        friday("19:50", "20:50", "21:50", "23:10")
        saturday("12:44", "15:14", "16:44", "18:50", "19:50", "20:50", "21:50", "23:10")
        monday("08:50", "10:50", "13:50", "15:50")
    }
    addStation("Hagstedt, Hagstedt") {
        friday("19:51", "20:51", "21:51", "23:11")
        saturday("12:42", "15:16", "16:42", "18:51", "19:51", "20:51", "21:51", "23:11")
        monday("08:51", "10:51", "13:51", "15:51")
    }
    addStation("Erlte, Abzweig Meyerhöfen") {
        friday("19:52", "20:52", "21:52", "23:12")
        saturday("12:40", "15:18", "16:40", "18:52", "19:52", "20:52", "21:52", "23:12")
        monday("08:52", "10:52", "13:52", "15:52")
    }
    addStation("Erlte, Hogeback") {
        friday("19:55", "20:55", "21:55", "23:15")
        saturday("12:38", "15:20", "16:38", "18:55", "19:55", "20:55", "21:55", "23:15")
        monday("08:55", "10:55", "13:55", "15:55")
    }
    addStation("Visbek, Elektro Schulz") {
        friday("19:57", "20:57", "21:57", "23:17")
        saturday("12:35", "15:23", "16:35", "18:57", "19:57", "20:57", "21:57", "23:17")
        monday("08:57", "10:57", "13:57", "15:57")
    }
    addStation("Visbek, Markt") {
        friday("20:00", "21:00", "22:00", "23:20")
        saturday("12:33", "15:33", "16:33", "19:00", "20:00", "21:00", "22:00", "23:20")
        monday("09:00", "11:00", "14:00", "16:00")
    }
    addStation("Visbek, Zur Tränke") {
        friday("20:03", "21:03", "22:03", "23:23")
        saturday("12:23", "15:35", "16:23", "19:03", "20:03", "21:03", "22:03", "23:23")
        monday("09:03", "11:03", "14:03", "16:03")
    }
    addStation("Astrup, Witte") {
        friday("20:06", "21:06", "22:06", "23:26")
        saturday("19:06", "20:06", "21:06", "22:06", "23:26")
        monday("09:06", "11:06", "14:06", "16:06")
    }
    addStation("Astrup, Lübberding") {
        friday("20:10", "21:10", "22:10", "23:30")
        saturday("12:11", "15:47", "16:11", "19:10", "20:10", "21:10", "22:10", "23:30")
        monday("09:10", "11:10", "14:10", "16:10")
    }
    addStation("Norddöllen, Frilling") {
        friday("20:12", "21:12", "22:12", "23:32")
        saturday("12:13", "15:45", "16:13", "19:12", "20:12", "21:12", "22:12", "23:32")
        monday("09:12", "11:12", "14:12", "16:12")
    }
    addStation("Norddöllen, Wempe") {
        friday("20:15", "21:15", "22:15", "23:35")
        saturday("12:15", "15:43", "16:15", "19:15", "20:15", "21:15", "22:15", "23:35")
        monday("09:15", "11:15", "14:15", "16:15")
    }
    addStation("Wöstendöllen") {
        saturday("12:18", "15:40", "16:18")
    }
    addStation("Stoppelmarkt") {
        isDestination = true
        friday("20:25", "21:25", "22:25", "23:45")
        saturday("12:50", "15:54", "16:50", "19:25", "20:25", "21:25", "22:25", "23:45")
        sunday("09:23", "11:23", "14:23", "16:23")
    }

    addReturnStation {
        title = "Stoppelmarkt („Hof Gisela“)"
        friday("22:30", "00:30", "02:00", "03:00", "04:30")
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
