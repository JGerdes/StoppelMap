package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.preparation.transportation.addReturnStation
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute
import com.jonasgerdes.stoppelmap.preparation.transportation.prices

internal fun langfoerden() = createBusRoute {
    title = "Langförden"
    additionalInfo =
        "Die Haltstellen Langförden-Mühle und Langförden-Scheele können aufgrund " +
                "einer Baustelle in diesem Jahr leider nicht bedient werden. " +
                "Bitte nutzen Sie die Haltestelle Langförden-Erzeugergroßmarkt."

    addStation("Deindrup, Kirschenweg") {
        prices(330, 250)

        thursday("18:30", "20:00")
        friday("15:00", "18:30", "20:00", "21:30", "22:30")
        saturday("15:00", "18:30", "20:00", "21:30", "22:30")
        sunday("15:00", "19:00")
        monday("09:00", "10:00", "13:00", "15:00", "18:00", "19:00", "20:00")
        tuesday("15:00", "19:00")
    }
    addStation("Deindrup, Schule", minutesAfterPrevious = 2) { prices(330, 250) }
    addStation("Deindrup, Spelgenweg", minutesAfterPrevious = 2) { prices(330, 250) }
    addStation("Spreda, Dr. Siemer", minutesAfterPrevious = 2) { prices(330, 250) }
    addStation("Spreda, Moormann", minutesAfterPrevious = 2) { prices(330, 250) }
    addStation("Langförden, Laurentiusplatz", minutesAfterPrevious = 5) { prices(300, 220) }
    addStation("Langförden, Erzeugergroßmarkt", minutesAfterPrevious = 2) { prices(300, 220) }
    addStation("Holtrup, Schule", minutesAfterPrevious = 5) { prices(260, 180) }
    addStation("Stoppelmarkt (»HofGisela«)", minutesAfterPrevious = 7) { isDestination = true }


    addReturnStation {
        title = "Stoppelmarkt"
        thursday("00:00", "01:00")
        friday("19:30", "22:00", "01:00", "02:00", "03:00", "04:00", "05:00")
        saturday("19:30", "22:00", "01:00", "02:00", "03:00", "04:00", "05:00")
        sunday("20:00", "21:00", "00:00")
        monday(
            "12:30",
            "14:30",
            "16:30",
            "18:30",
            "19:30",
            "22:00",
            "23:00",
            "00:00",
            "01:00",
            "02:00"
        )
        thursday("19:30", "23:30")
    }
}
