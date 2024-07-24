package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.preparation.dsl.operator

object TransportOperators {
    val wilmering = operator("Wilmering") {
        websites += "https://wilmering-bewegt.de/fahrplaene/stoppelmarkt/"
        websites += "https://www.instagram.com/wilmering.bewegt/"
        websites += "https://www.facebook.com/wilmering.bewegt"
    }

    fun all() = listOf(wilmering)
}