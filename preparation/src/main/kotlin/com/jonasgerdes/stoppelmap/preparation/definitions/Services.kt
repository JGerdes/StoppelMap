package com.jonasgerdes.stoppelmap.preparation.definitions

import com.jonasgerdes.stoppelmap.dto.Locales.de
import com.jonasgerdes.stoppelmap.dto.Locales.en
import com.jonasgerdes.stoppelmap.dto.data.Alias
import com.jonasgerdes.stoppelmap.dto.data.PhoneNumber
import com.jonasgerdes.stoppelmap.dto.data.Service
import com.jonasgerdes.stoppelmap.preparation.definitions.ServiceSlugs.atm
import com.jonasgerdes.stoppelmap.preparation.definitions.ServiceSlugs.fireDepartment
import com.jonasgerdes.stoppelmap.preparation.definitions.ServiceSlugs.lostAndFound
import com.jonasgerdes.stoppelmap.preparation.definitions.ServiceSlugs.medicalService
import com.jonasgerdes.stoppelmap.preparation.definitions.ServiceSlugs.police
import com.jonasgerdes.stoppelmap.preparation.localizedString

object ServiceSlugs {
    const val atm = "service_atm"
    const val police = "service_police"
    const val medicalService = "service_medical"
    const val fireDepartment = "service_fire_department"
    const val lostAndFound = "service_lost_and_found"
}

val services = listOf(
    Service(
        slug = atm,
        name = localizedString(
            de = "Geldautomat",
            en = "Cash machine"
        ),
        aliases = listOf(
            Alias("ATM", en)
        ),
    ),
    Service(
        slug = police,
        name = localizedString(
            de = "Polizei",
            en = "Police"
        ),
        phoneNumbers = listOf(
            PhoneNumber("+4944418547800", "04441/85 47 800")
        )
    ),
    Service(
        slug = medicalService,
        name = localizedString(
            de = "Sanitätsdienst",
            en = "Medical service"
        ),
        phoneNumbers = listOf(
            PhoneNumber("+4944418547770", "04441/85 47 77 0")
        ),
        aliases = listOf(
            Alias("Erste Hilfe", de),
            Alias("DRK", de),
            Alias("Deutsches Rotes Kreuz", de),
            Alias("First aid", en),
            Alias("Red Cross", en)
        ),
    ),
    Service(
        slug = fireDepartment,
        name = localizedString(
            de = "Feuerwehr",
            en = "Fire department"
        ),
        phoneNumbers = listOf(
            PhoneNumber("+4944418547712", "04441/8 54 77 12")
        ),
        aliases = listOf(
            Alias("Fire brigade", en),
        ),
    ),
    Service(
        slug = lostAndFound,
        name = localizedString(
            de = "Marktverwaltung / Fundbüro",
            en = "Management / lost-and-found office"
        ),
        phoneNumbers = listOf(
            PhoneNumber("+4944419373730", "04441/8547770")
        ),
        note = localizedString(
            de = "täglich bis 24:00 Uhr",
            en = "everyday until midnight",
        ),
    ),
)