package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.dto.data.Route
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.dinklage
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.holdorfBrockdorf
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.langenbergMuehlen
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.lohneMoorkamp
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.lohneStadt

fun generateBusRoutes() = listOf<Route>(
    lohneStadt(),
    lohneMoorkamp(),
    dinklage(),
    langenbergMuehlen(),
    holdorfBrockdorf(),
//    barnstorf(),
//    ellenstedtGoldenstedtLutten(),
//    cloppenburg(),
//    lindern(),
//    essen(),
//    loeningen(),
//    friesoythe(),
).sortedBy { it.name }

fun generateTrainRoutes() = listOf<Route>(
    /*bremen(),
    osnabrueck(),*/
).sortedBy { it.name }