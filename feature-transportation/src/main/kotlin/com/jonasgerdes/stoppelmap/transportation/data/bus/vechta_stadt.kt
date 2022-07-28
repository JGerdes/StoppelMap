import com.jonasgerdes.stoppelmap.transportation.data.*

internal fun vechtaStadt() = createBusRoute {
    title = "Vechta Stadt"
    fixedPrices = prices(adult = 250, children = 180)

    addStation("Sgundek") {
        thursday {
            "18:30" every 15.Minutes until "02:45"
        }
        friday {
            "15:00" every 30.Minutes until "21:00"
            "21:00" every 15.Minutes until "03:45"
        }
        saturday {
            "15:00" every 30.Minutes until "21:00"
            "21:00" every 15.Minutes until "03:45"
        }
        sunday {
            "13:00" every 60.Minutes until "00:00"
        }
        monday {
            "08:00" every 30.Minutes until "10:30"
            "10:30" every 15.Minutes until "21:00"
            "21:00" every 30.Minutes until "02:30"
        }
        tuesday {
            "14:00" every 60.Minutes until "20:00"
        }
    }
    addStation("Am Schützenplatz", minutesAfterPrevious = 1)
    addStation("Münsterstraße, Wessel", minutesAfterPrevious = 2)
    addStation("Münsterstraße/Hagener Straße", minutesAfterPrevious = 2)
    addStation("Burgstraße, Altes Finanzamt", minutesAfterPrevious = 3)
    addStation("Klingenhagen", minutesAfterPrevious = 1)
    addStation("Füchteler Straße/Krusenschlopp", minutesAfterPrevious = 1)
    addStation("Botenkamp/Georgstraße", minutesAfterPrevious = 2)
    addStation("Botenkamp/Markusstraße", minutesAfterPrevious = 1)
    addStation("Stoppelmarkt", minutesAfterPrevious = 2)

    addReturnStation {
        title = "Stoppelmarkt"
        thursday {
            "19:00" every 15.Minutes until "03:00"
        }
        friday {
            "15:15" every 30.Minutes until "21:15"
            "21:15" every 15.Minutes until "04:00"
            laterDeparturesOnDemand = true
        }
        saturday {
            "15:15" every 30.Minutes until "21:15"
            "21:15" every 15.Minutes until "04:00"
            laterDeparturesOnDemand = true
        }
        sunday {
            "13:30" every 60.Minutes until "00:30"
        }
        monday {
            "10:30" every 15.Minutes until "21:30"
            "21:30" every 30.Minutes until "03:00"
        }
        tuesday {
            "14:30" every 60.Minutes until "20:30"
            departures("23:00")
        }
    }
}
