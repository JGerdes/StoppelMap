package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.preparation.transportation.bus.bakumEssen
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.barnstorf
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.bremen
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.calveslage
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.diepholz
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.dinklage
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.ellenstedtGoldenstedtLutten
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.holdorfBrockdorf
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.langenbergMuehlen
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.langfoerden
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.lohneMoorkamp
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.lohneStadt
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.osnabrueck
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaFlugplatz
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaStadt
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaSued
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaTelbrake
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaWest
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.visbek

fun generateRoutes() = listOf(
    bremen(),
    osnabrueck(),
    vechtaStadt(),
    vechtaFlugplatz(),
    vechtaSued(),
    vechtaTelbrake(),
    vechtaWest(),
    langfoerden(),
    calveslage(),
    diepholz(),
    barnstorf(),
    bakumEssen(),
    visbek(),
    ellenstedtGoldenstedtLutten(),
    lohneStadt(),
    lohneMoorkamp(),
    dinklage(),
    langenbergMuehlen(),
    holdorfBrockdorf()
).sortedBy { it.title }
