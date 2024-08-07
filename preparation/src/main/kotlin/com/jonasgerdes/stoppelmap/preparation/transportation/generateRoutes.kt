package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.dto.data.Route
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.barnstorf
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.cloppenburg
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.dinklage
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.ellenstedtGoldenstedtLutten
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.essen
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.friesoythe
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.holdorfBrockdorf
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.langenbergMuehlen
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.lindern
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.loeningen
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.lohneMoorkamp
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.lohneStadt
import com.jonasgerdes.stoppelmap.preparation.transportation.bus.oldenburg

fun generateBusRoutes() = listOf<Route>(
    lohneStadt(),
    lohneMoorkamp(),
    dinklage(),
    langenbergMuehlen(),
    holdorfBrockdorf(),
    barnstorf(),
    ellenstedtGoldenstedtLutten(),
    cloppenburg(),
    lindern(),
    essen(),
    loeningen(),
    friesoythe(),
    oldenburg()
).sortedBy { it.name }

fun generateTrainRoutes() = listOf<Route>(
    /*bremen(),
    osnabrueck(),*/
).sortedBy { it.name }