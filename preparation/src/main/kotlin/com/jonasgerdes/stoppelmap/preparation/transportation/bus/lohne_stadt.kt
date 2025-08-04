package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.schomaker
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun lohneStadt() = createBusRoute {
    name = "Lohne (Stadt)"
    operatorSlug = schomaker
    arrivalStationSlug = busbahnhofOst
    fixedPrices = prices(adult = 400, children = 200, 3 to 14)

    returns {
        thursday {
            "19:15" every 30.Minutes until "02:15"
        }
        friday {
            "16:45" every 30.Minutes until "19:15"
            "19:45" every 15.Minutes until "03:45"
        }
        saturday {
            "16:45" every 30.Minutes until "19:15"
            "19:45" every 15.Minutes until "03:45"
        }
        sunday {
            "17:15" every 30.Minutes until "23:45"
        }
        monday {
            "11:45" every 15.Minutes until "02:15"
        }
        tuesday {
            "17:15" every 30.Minutes until "02:15"
        }
    }

    addStation("Busbahnhof, Falkenbergstraße") {
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
                "09:00" every 15.Minutes until "21:00"
            }
            tuesday {
                "15:00" every 30.Minutes until "21:00"
            }
        }
    }


    addStation("Brinkstraße, Landwehr", minutesAfterPrevious = 3)
    addStation("Hamberg, Haltestelle", minutesAfterPrevious = 3)
    addStation("Bergweg, Bruno Kleine", minutesAfterPrevious = 3)
    addStation("Bergweg, Felta-Tankstelle", minutesAfterPrevious = 2)
    addStation("Brägeler Straße, Imbiß Zerhusen", minutesAfterPrevious = 2)
    addStation("Lindenstraße, Kino", minutesAfterPrevious = 2)
    addStation("Lindenstraße, ehem. Schomaker", minutesAfterPrevious = 2)
    addStation("Nordlohne - Gastw. Brinkmann", minutesAfterPrevious = 2)
}
