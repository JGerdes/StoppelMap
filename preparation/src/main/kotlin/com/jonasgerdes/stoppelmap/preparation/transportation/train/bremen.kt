package com.jonasgerdes.stoppelmap.preparation.transportation.train

import com.jonasgerdes.stoppelmap.preparation.transportation.Minutes
import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute

fun bremen() = createBusRoute {
    name = "RB58 Bremen"

    addStation("Bremen Hbf") {
        thursday {
            "06:19" every 60.Minutes until "20:19"
            departures("21:20", "22:19", "23:10")
        }
        friday {
            "06:19" every 60.Minutes until "20:19"
            departures("21:20", "22:19", "23:19", "00:22", "01:35")
        }
        saturday {
            "06:19" every 60.Minutes until "20:19"
            departures("21:20", "22:19", "23:19", "00:22", "01:35")
        }
        sunday {
            "07:19" every 60.Minutes until "20:19"
            departures("21:20", "22:19", "23:19", "01:35")
        }
        monday {
            "06:19" every 60.Minutes until "20:19"
            departures("21:20", "22:19", "23:19", "01:35")
        }
        tuesday {
            "06:19" every 60.Minutes until "20:19"
            departures("21:20", "22:19", "23:19", "01:35")
        }
    }
    addStation("Bremen Neustadt", minutesAfterPrevious = 4)
    addStation("Delmenhorst") {
        thursday {
            "06:32" every 60.Minutes until "20:32"
            departures("21:33", "22:32", "23:33")
        }
        friday {
            "06:32" every 60.Minutes until "20:32"
            departures("21:33", "22:32", "23:33", "00:35", "01:47")
        }
        saturday {
            "06:32" every 60.Minutes until "20:32"
            departures("21:33", "22:32", "23:33", "00:35", "01:47")
        }
        sunday {
            "07:32" every 60.Minutes until "20:32"
            departures("21:33", "22:32", "23:33", "01:47", "01:47")
        }
        monday {
            "06:32" every 60.Minutes until "20:32"
            departures("21:33", "22:32", "23:33", "01:47")
        }
        tuesday {
            "06:32" every 60.Minutes until "20:32"
            departures("21:33", "22:32", "23:33", "01:47")
        }
    }
    addStation("Ganderkesee") {
        thursday {
            "06:38" every 60.Minutes until "20:38"
            departures("21:40", "22:38", "23:40")
        }
        friday {
            "06:38" every 60.Minutes until "20:38"
            departures("21:40", "22:38", "23:40", "00:41", "01:53")
        }
        saturday {
            "06:38" every 60.Minutes until "20:38"
            departures("21:40", "22:38", "23:40", "00:41", "01:53")
        }
        sunday {
            "07:38" every 60.Minutes until "20:38"
            departures("21:40", "22:38", "23:40", "00:41", "01:53")
        }
        monday {
            "06:38" every 60.Minutes until "20:38"
            departures("21:40", "22:38", "23:40", "01:53")
        }
        tuesday {
            "06:38" every 60.Minutes until "20:38"
            departures("21:40", "22:38", "23:40", "01:53")
        }
    }
    addStation("Brettorf", minutesAfterPrevious = 9)
    addStation("Wildeshausen") {
        thursday {
            "06:00" every 60.Minutes until "21:00"
            departures("22:00", "23:00", "00:00")
        }
        friday {
            "06:00" every 60.Minutes until "21:00"
            departures("22:00", "23:00", "00:00", "01:00", "02:16", "03:30")
        }
        saturday {
            "07:00" every 60.Minutes until "21:00"
            departures("22:00", "23:00", "00:00", "01:00", "02:16", "03:30")
        }
        sunday {
            "08:00" every 60.Minutes until "21:00"
            departures("22:00", "23:00", "00:00", "01:00", "02:16")
        }
        monday {
            "06:00" every 60.Minutes until "21:00"
            departures("22:00", "23:00", "00:00", "01:00")
        }
        tuesday {
            "06:00" every 60.Minutes until "21:00"
            departures("22:00", "23:00", "00:00", "01:00")
        }
    }
    addStation("Rechterfeld", minutesAfterPrevious = 7)
    addStation("Goldenstedt", minutesAfterPrevious = 4)
    addStation("Lutten", minutesAfterPrevious = 5)
    addStation("Stoppelmarkt", minutesAfterPrevious = 5) {
        isDestination = true
    }
    addReturnStation {
        name = "Stoppelmarkt"

        thursday {
            departures("04:33", "05:33", "06:33", "07:34")
            "08:33" every 60.Minutes until "22:33"
            departures("00:34", "04:33")
        }
        friday {
            departures("05:33", "06:33", "07:34")
            "08:33" every 60.Minutes until "22:33"
            departures("23:35", "00:34", "01:50", "03:06", "04:04")
        }
        saturday {
            departures("05:33", "06:33", "07:34")
            "08:33" every 60.Minutes until "22:33"
            departures("23:35", "00:34", "01:50", "03:06", "04:04")
        }
        sunday {
            departures("06:33", "07:34")
            "08:33" every 60.Minutes until "22:33"
            departures("04:33", "05:33")
        }
        monday {
            departures("06:33", "07:34")
            "08:33" every 60.Minutes until "22:33"
            departures("04:33", "05:33")
        }
        tuesday {
            departures("06:33", "07:34")
            "08:33" every 60.Minutes until "22:33"
            departures("04:33", "05:33")
        }
    }
}
