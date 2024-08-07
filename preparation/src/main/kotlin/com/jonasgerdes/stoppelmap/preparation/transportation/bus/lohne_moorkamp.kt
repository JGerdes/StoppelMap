package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.schomaker
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun lohneMoorkamp() = createBusRoute {
    name = "Lohne (Voßberg - Moorkamp - Rießel)"
    operatorSlug = schomaker
    arrivalStationSlug = busbahnhofOst
    fixedPrices = prices(adult = 400, children = 200, 3 to 14)

    returns {
        thursday {
            "19:10" every 30.Minutes until "02:10"
        }
        friday {
            "16:40" every 30.Minutes until "20:10"
            "20:40" every 15.Minutes until "04:55"
        }
        saturday {
            "15:40" every 30.Minutes until "20:10"
            "20:40" every 15.Minutes until "04:55"
        }
        sunday {
            "15:40" every 30.Minutes until "23:40"
        }
        monday {
            "11:40" every 15.Minutes until "02:55"
        }
        tuesday {
            "17:10" every 30.Minutes until "02:10"
        }
    }

    addStation("Jägerstraße/ Wangerooger Str.") {
        outward {
            thursday {
                "17:00" every 30.Minutes until "23:00"
            }
            friday {
                "15:00" every 30.Minutes until "20:00"
                "20:00" every 15.Minutes until "01:00"
            }
            saturday {
                "14:00" every 30.Minutes until "20:00"
                "20:00" every 15.Minutes until "01:00"
            }
            sunday {
                "14:00" every 30.Minutes until "22:00"
            }
            monday {
                "08:30" every 15.Minutes until "22:00"
            }
            tuesday {
                "15:00" every 30.Minutes until "22:00"
            }
        }
    }

    addStation("Voßberger Str./Reinekestr.", minutesAfterPrevious = 2)
    addStation("Bakumer Str./ Luchsweg", minutesAfterPrevious = 2)
    addStation("Märschendorfer Str./ Bruchweg", minutesAfterPrevious = 2)
    addStation("Brandstraße/ Stratmanns Hotel", minutesAfterPrevious = 1)
    addStation("Brandstraße/ Unter den Erlen", minutesAfterPrevious = 1)
    addStation("Rießel/ Dorfplatz", minutesAfterPrevious = 2)

}
