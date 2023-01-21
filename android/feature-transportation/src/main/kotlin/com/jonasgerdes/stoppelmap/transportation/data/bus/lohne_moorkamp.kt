package com.jonasgerdes.stoppelmap.transportation.data.bus

import com.jonasgerdes.stoppelmap.transportation.data.*

internal fun lohne_moorkamp() = createBusRoute {
    title = "Lohne (Voßberg - Moorkamp - Rießel)"
    fixedPrices = prices(adult = 400, children = 200, 3 to 14)

    addStation("Jägerstraße/ Wangerooger Str.") {
        thursday {
            "17:15" every 30.Minutes until "00:15"
        }
        friday {
            "15:15" every 30.Minutes until "18:45"
            "18:45" every 15.Minutes until "20:00"
            "20:00" every 12.Minutes until "01:00"
        }
        saturday {
            "14:10" every 20.Minutes until "18:50"
            "19:00" every 15.Minutes until "20:00"
            "20:00" every 12.Minutes until "01:00"
        }
        sunday {
            "14:15" every 30.Minutes until "23:15"
        }
        monday {
            "08:00" every 15.Minutes until "00:00"
        }
        tuesday {
            "15:15" every 30.Minutes until "00:15"
        }
    }

    addStation("Voßberger Str./Reinekestr.", minutesAfterPrevious = 2)
    addStation("Bakumer Str./ Luchsweg", minutesAfterPrevious = 2)
    addStation("Märschendorfer Str./ Bruchweg", minutesAfterPrevious = 2)
    addStation("Brandstraße/ Stratmanns Hotel", minutesAfterPrevious = 1)
    addStation("Brandstraße/ Unter den Erlen", minutesAfterPrevious = 1)
    addStation("Rießel/ Dorfplatz", minutesAfterPrevious = 2)
    addStation("Stoppelmarkt", minutesAfterPrevious = 15) { isDestination = true }

    addReturnStation {
        title = "Stoppelmarkt"
        thursday {
            "17:55" every 30.Minutes until "01:55"
        }
        friday {
            "15:55" every 30.Minutes until "19:25"
            "19:40" every 15.Minutes until "20:25"
            "20:40" every 12.Minutes until "05:00"
        }
        saturday {
            "14:50" every 20.Minutes until "19:30"
            "19:40" every 15.Minutes until "20:25"
            "20:40" every 12.Minutes until "05:00"
        }
        sunday {
            "14:55" every 30.Minutes until "00:45"
        }
        monday {
            "08:40" every 15.Minutes until "03:00"
        }
        tuesday {
            "15:55" every 30.Minutes until "01:55"
        }
    }
}
