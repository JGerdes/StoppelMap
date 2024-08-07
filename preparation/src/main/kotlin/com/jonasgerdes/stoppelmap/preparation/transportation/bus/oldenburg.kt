package com.jonasgerdes.stoppelmap.preparation.transportation.bus

import com.jonasgerdes.stoppelmap.dto.data.Website
import com.jonasgerdes.stoppelmap.preparation.localizedString
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportMapEntitySlugs.busbahnhofOst
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.ols
import com.jonasgerdes.stoppelmap.preparation.transportation.addStation
import com.jonasgerdes.stoppelmap.preparation.transportation.createBusRoute

internal fun oldenburg() = createBusRoute {
    name = "Oldenburg"
    arrivalStationSlug = busbahnhofOst
    operatorSlug = ols
    ticketWebsites += Website(
        url = "https://ols.reservix.de/p/reservix/group/476149"
    )
    additionalInfo = localizedString(de = "Tickets nur online erhältlich.", en = "Tickets available online only.")

    returns {
        friday("00:00", "02:30")
        saturday("00:00", "02:00")
        monday("16:30", "18:00")
    }

    addStation("ZOB Oldenburg") {

        outward {
            friday("17:00", "19:00")
            saturday("17:00", "19:00")
            monday("09:00", "11:30")
        }
    }
}
