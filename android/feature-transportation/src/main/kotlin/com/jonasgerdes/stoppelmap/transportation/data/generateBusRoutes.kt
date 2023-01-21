package com.jonasgerdes.stoppelmap.transportation.data

import com.jonasgerdes.stoppelmap.transportation.data.bus.*
import vechtaFlugplatz
import vechtaStadt

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
