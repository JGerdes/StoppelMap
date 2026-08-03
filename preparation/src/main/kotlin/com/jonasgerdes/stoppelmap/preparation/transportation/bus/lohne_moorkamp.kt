package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.schomaker
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices
import com.jonasgerdes.stoppelmap.preparation.transportation.schomakerSundayDealPrices

internal fun lohneMoorkamp() = createBusRoute {
    name = "Lohne (Voßberg - Moorkamp - Rießel)"
    operatorSlug = schomaker
    arrivalStationSlug = busbahnhofOst
    fixedPrices = prices(adult = 500, children = 250, 3 to 14) + schomakerSundayDealPrices

    returns {
        thursday {
            "19:10" every 30.Minutes until "02:10"
        }
        friday {
            "16:40" every 30.Minutes until "19:10"
            "19:40" every 15.Minutes until "03:40"
        }
        saturday {
            "16:40" every 30.Minutes until "19:10"
            "19:40" every 15.Minutes until "03:40"
        }
        sunday {
            "17:10" every 30.Minutes until "23:40"
        }
        monday {
            "11:40" every 15.Minutes until "00:10"
        }
        tuesday {
            "17:10" every 30.Minutes until "01:10"
        }
    }

    addStation("Jägerstraße (Wangerooger Str.)") {
        outward {
            thursday {
                "17:00" every 30.Minutes until "23:00"
            }
            friday {
                "15:00" every 30.Minutes until "19:00"
                "19:00" every 15.Minutes until "23:30"
            }
            saturday {
                "14:00" every 30.Minutes until "19:00"
                "19:00" every 15.Minutes until "23:30"
            }
            sunday {
                "14:00" every 30.Minutes until "21:00"
            }
            monday {
                "09:00" every 15.Minutes until "22:00"
            }
            tuesday {
                "15:00" every 30.Minutes until "21:00"
            }
        }
    }

    addStation("Reinekestraße (Kreuzanlage)", minutesAfterPrevious = 2)
    addStation("Luchsweg", minutesAfterPrevious = 2)
    addStation("Märschendorfer Str./Bruchweg", minutesAfterPrevious = 2)
    addStation("Brandstraße (Stratmanns Hotel)", minutesAfterPrevious = 1)
    addStation("Unter den Erlen", minutesAfterPrevious = 1)
    addStation("Rießel (Dorfplatz)", minutesAfterPrevious = 2)
    addStation("Nasch", minutesAfterPrevious = 2)

}
