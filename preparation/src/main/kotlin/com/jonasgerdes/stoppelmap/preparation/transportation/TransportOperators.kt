package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.preparation.dsl.operator
import com.jonasgerdes.stoppelmap.preparation.dsl.website

object TransportOperators {
    val wilmering = operator("Wilmering") {
        website("https://wilmering-bewegt.de/fahrplaene/stoppelmarkt/")
        website("https://www.instagram.com/wilmering.bewegt/")
        website("https://www.facebook.com/wilmering.bewegt")
    }

    fun all() = listOf(wilmering)
}