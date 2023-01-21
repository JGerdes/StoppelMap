import com.jonasgerdes.stoppelmap.transportation.data.*

internal fun vechtaFlugplatz() = createBusRoute {
    title = "Vechta Flugplatz"
    fixedPrices = prices(adult = 250, children = 180)

    addStation(title = "Von-Ascheberg-Straße/Kindergarten") {
        isNew = true

        thursday {
            "18:23" every 30.Minutes until "02:23"
        }
        friday {
            "14:53" every 60.Minutes until "17:53"
            "17:53" every 30.Minutes until "03:23"
        }
        saturday {
            "13:53" every 60.Minutes until "17:53"
            "17:53" every 30.Minutes until "03:23"
        }
        sunday {
            "12:53" every 60.Minutes until "23:53"
        }
        monday {
            "07:53" every 60.Minutes until "01:53"
        }
        tuesday {
            "13:53" every 60.Minutes until "19:53"
        }
    }
    addStation(title = "Hagen-Ringstraße", minutesAfterPrevious = 2) {
        isNew = true
    }
    addStation(title = "Vechtaer Marsch/Straßburger Straße", minutesAfterPrevious = 4)
    addStation(title = "Vechtaer Marsch/Dornierstraße", minutesAfterPrevious = 1)
    addStation(title = "Vechtaer Marsch", minutesAfterPrevious = 1)
    addStation(title = "Vechtaer Marsch/Wienerstraße", minutesAfterPrevious = 2)
    addStation(title = "Stukenborg", minutesAfterPrevious = 4)
    addStation(title = "Graskamp/Herberskamp", minutesAfterPrevious = 1)
    addStation(title = "Graskamp/Hirsekamp", minutesAfterPrevious = 1)
    addStation(title = "Sandeskamp/Riedenkamp", minutesAfterPrevious = 2)
    addStation(title = "Schweriner Straße/Petersburger Straße", minutesAfterPrevious = 2)
    addStation(title = "Allensteiner Straße/Fahrschule Moss", minutesAfterPrevious = 2)
    addStation(title = "Allensteiner Straße/Dresdener Straße", minutesAfterPrevious = 1)
    addStation(title = "Stoppelmarkt", minutesAfterPrevious = 4) { isDestination = true }

    addReturnStation {
        title = "Stoppelmarkt"
        thursday {
            "19:00" every 30.Minutes until "03:00"
        }
        friday {
            "15:30" every 60.Minutes until "18:30"
            "18:30" every 30.Minutes until "04:00"
            laterDeparturesOnDemand = true
        }
        saturday {
            "14:30" every 60.Minutes until "18:30"
            "18:30" every 30.Minutes until "04:00"
            laterDeparturesOnDemand = true
        }
        sunday {
            "13:30" every 60.Minutes until "00:30"
        }
        monday {
            "10:30" every 60.Minutes until "02:30"
        }
        tuesday {
            "14:30" every 60.Minutes until "20:30"
            departures("23:00")
        }
    }
}
