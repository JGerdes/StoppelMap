package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.preparation.dsl.operator
import com.jonasgerdes.stoppelmap.preparation.dsl.website
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.wilmering

object TransportOperatorSlugs {
    val wilmering = "operator_wilmering"
}

val transportOperators = listOf(
    operator(slug = wilmering, name = "Wilmering") {
        website("https://wilmering-bewegt.de/fahrplaene/stoppelmarkt/")
        website("https://www.instagram.com/wilmering.bewegt/")
        website("https://www.facebook.com/wilmering.bewegt")
    }
)