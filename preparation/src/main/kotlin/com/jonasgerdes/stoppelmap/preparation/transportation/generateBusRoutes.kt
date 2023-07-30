package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.preparation.transportation.bus.bakumEssen
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.barnstorf
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.calveslage
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.diepholz
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.dinklage
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.ellenstedtGoldenstedtLutten
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.holdorf_brockdorf
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.langenberg_muehlen
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.langfoerden
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.lohne_moorkamp
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.lohne_stadt
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaFlugplatz
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaStadt
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaSued
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaTelbrake
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.vechtaWest
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.visbek

fun generateBusRoutes() = listOf(
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
    lohne_stadt(),
    lohne_moorkamp(),
    dinklage(),
    langenberg_muehlen(),
    holdorf_brockdorf()
).sortedBy { it.title }
