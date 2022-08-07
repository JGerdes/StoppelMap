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
    ellenstedtGoldenstedtLutten()
).sortedBy { it.title }
