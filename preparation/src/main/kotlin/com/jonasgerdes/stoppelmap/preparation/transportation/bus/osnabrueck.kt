package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.data.model.database.RouteType
import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute

fun osnabrueck() = createBusRoute {
    title = "RB58 Osnabrück"
    type = RouteType.Train

    addStation("Osnabrück Hbf") {
        thursday {
            "06:24" every 60.Minutes until "21:24"
            departures("23:24")
        }
        friday {
            "06:24" every 60.Minutes until "21:24"
            departures("22:26", "23:24")
        }
        saturday {
            "06:24" every 60.Minutes until "21:24"
            departures("22:26", "23:24")
        }
        sunday {
            "07:24" every 60.Minutes until "21:24"
            departures("23:24")
        }
        monday {
            "06:24" every 60.Minutes until "20:24"
            departure("20:38", annotation = "Schienenersatzverkehr")
            departure("22:38", annotation = "Schienenersatzverkehr")
        }
        tuesday {
            "06:24" every 60.Minutes until "21:24"
            departures("23:24")
        }
    }

    addStation("Osnabrück Altstadt") {
        thursday {
            "06:26" every 60.Minutes until "21:26"
            departures("23:26")
        }
        friday {
            "06:26" every 60.Minutes until "21:26"
            departures("22:26", "23:26")
        }
        saturday {
            "06:26" every 60.Minutes until "21:26"
            departures("22:26", "23:26")
        }
        sunday {
            "07:26" every 60.Minutes until "21:26"
            departures("23:26")
        }
        monday {
            "06:26" every 60.Minutes until "20:26"
            departure("20:44", annotation = "Schienenersatzverkehr")
            departure("22:44", annotation = "Schienenersatzverkehr")
        }
        tuesday {
            "06:26" every 60.Minutes until "21:26"
            departures("23:26")
        }
    }

    addStation("Halen") {
        thursday {
            "06:33" every 60.Minutes until "21:33"
            departures("23:33")
        }
        friday {
            "06:33" every 60.Minutes until "21:33"
            departures("22:33", "23:33")
        }
        saturday {
            "06:33" every 60.Minutes until "21:33"
            departures("22:33", "23:33")
        }
        sunday {
            "07:33" every 60.Minutes until "21:33"
            departures("23:33")
        }
        monday {
            "06:33" every 60.Minutes until "20:33"
            departure("21:03", annotation = "Schienenersatzverkehr")
            departure("23:03", annotation = "Schienenersatzverkehr")
        }
        tuesday {
            "06:33" every 60.Minutes until "21:33"
            departures("23:33")
        }
    }

    addStation("Achmer") {
        thursday {
            "06:38" every 60.Minutes until "21:38"
            departures("23:38")
        }
        friday {
            "06:38" every 60.Minutes until "21:38"
            departures("22:38", "23:38")
        }
        saturday {
            "06:38" every 60.Minutes until "21:38"
            departures("22:38", "23:38")
        }
        sunday {
            "07:38" every 60.Minutes until "21:38"
            departures("23:38")
        }
        monday {
            "06:38" every 60.Minutes until "20:38"
            departure("21:18", annotation = "Schienenersatzverkehr")
            departure("23:18", annotation = "Schienenersatzverkehr")
        }
        tuesday {
            "06:38" every 60.Minutes until "21:38"
            departures("23:38")
        }
    }

    addStation("Bramsche") {
        thursday {
            "06:44" every 60.Minutes until "21:44"
            departures("23:44")
        }
        friday {
            "06:44" every 60.Minutes until "21:44"
            departures("22:44", "23:44")
        }
        saturday {
            "06:44" every 60.Minutes until "21:44"
            departures("22:44", "23:44")
        }
        sunday {
            "07:44" every 60.Minutes until "21:44"
            departures("23:44")
        }
        monday {
            "06:44" every 60.Minutes until "21:44"
            departures("23:44")
        }
        tuesday {
            "06:44" every 60.Minutes until "21:44"
            departures("23:44")
        }
    }

    addStation("Hesepe", minutesAfterPrevious = 3)
    addStation("Rieste", minutesAfterPrevious = 6)
    addStation("Neuenkirchen") {
        thursday {
            "06:59" every 60.Minutes until "21:59"
            departures("23:59")
        }
        friday {
            "06:59" every 60.Minutes until "23:59"
            departures("01:16", "02:31", "03:39")
        }
        saturday {
            "06:59" every 60.Minutes until "23:59"
            departures("01:16", "02:31", "03:39")
        }
        sunday {
            "07:59" every 60.Minutes until "21:59"
            departures("23:59")
        }
        monday {
            "06:59" every 60.Minutes until "21:59"
            departures("23:59")
        }
        tuesday {
            "06:59" every 60.Minutes until "21:59"
            departures("23:59")
        }
    }
    addStation("Holdorf", minutesAfterPrevious = 8)
    addStation("Steinfeld", minutesAfterPrevious = 5)
    addStation("Mühlen", minutesAfterPrevious = 3)
    addStation("Lohne") {
        thursday {
            "06:21" every 60.Minutes until "22:21"
            departures("00:21")
        }
        friday {
            "06:21" every 60.Minutes until "00:21"
        }
        saturday {
            "06:21" every 60.Minutes until "00:21"
            departures("01:39", "02:53", "03:51")
        }
        sunday {
            "06:21" every 60.Minutes until "22:21"
            departures("00:21")
        }
        monday {
            "06:21" every 60.Minutes until "22:21"
            departures("00:21")
        }
        tuesday {
            "06:21" every 60.Minutes until "22:21"
            departures("00:21")
        }
    }

    addStation("Vechta") {
        thursday {
            departures("06:30", "06:30", "07:31")
            "08:30" every 60.Minutes until "22:30"
            departures("00:31")
        }
        friday {
            departures("06:30", "06:30", "07:31")
            "08:30" every 60.Minutes until "22:30"
            departures("23:31", "00:31", "01:47", "03:03", "04:01")
        }
        saturday {
            departures("06:30", "06:30", "07:31")
            "08:30" every 60.Minutes until "22:30"
            departures("23:31", "00:31", "01:47", "03:03", "04:01")
        }
        sunday {
            departures("06:30", "07:31")
            "08:30" every 60.Minutes until "22:30"
        }
        monday {
            departures("06:30", "06:30", "07:31")
            "08:30" every 60.Minutes until "22:30"
        }
        tuesday {
            departures("06:30", "06:30", "07:31")
            "08:30" every 60.Minutes until "22:30"
        }
    }

    addStation("Stoppelmarkt", minutesAfterPrevious = 3) {
        isDestination = true
    }
    addReturnStation {
        title = "Stoppelmarkt"

        thursday {
            "06:21" every 60.Minutes until "00:21"
        }
        friday {
            "06:21" every 60.Minutes until "01:21"
            departures("02:37", "03:51")
        }
        saturday {
            "07:21" every 60.Minutes until "01:21"
            departures("02:37", "03:51")
        }
        sunday {
            "08:21" every 60.Minutes until "00:21"
            departures("02:37")
        }
        monday {
            "06:21" every 60.Minutes until "00:21"
            departures("02:37")
        }
        tuesday {
            "06:21" every 60.Minutes until "00:21"
            departures("02:37")
        }
    }
}
