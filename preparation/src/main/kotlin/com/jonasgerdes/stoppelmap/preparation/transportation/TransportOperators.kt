package com.jonasgerdes.stoppelmap.preparation.transportation

import com.jonasgerdes.stoppelmap.preparation.dsl.operator
import com.jonasgerdes.stoppelmap.preparation.dsl.website
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.feldhaus
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.schomaker
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.vnn
import com.jonasgerdes.stoppelmap.preparation.transportation.TransportOperatorSlugs.wilmering

object TransportOperatorSlugs {
    val wilmering = "operator_wilmering"
    val schomaker = "operator_schomaker"
    val vnn = "operator_verkehrsbetriebe_vechta_nord"
    val feldhaus = "operator_feldhaus_reisen"
    val ols = "operator_ols"
}

val transportOperators = listOf(
    operator(slug = wilmering, name = "Wilmering") {
        website("https://wilmering-bewegt.de/fahrplaene/stoppelmarkt/")
        website("https://www.instagram.com/wilmering.bewegt/")
        website("https://www.facebook.com/wilmering.bewegt")
    },
    operator(slug = schomaker, name = "Schomaker") {
        website("https://www.schomaker-reisen.de/information.php?verteiler=Eventverkehr#fahrplan")
        website("https://www.instagram.com/Schomaker_Reisen")
        website("https://www.facebook.com/SchomakerReisen")
    },
    operator(slug = vnn, name = "Verkehrsbetriebe Vechta Nord") {
        website("https://www.instagram.com/verkehrsbetriebe_vechta_nord/")
    },
    operator(slug = feldhaus, name = "Feldhaus Reisen") {
        website("https://www.feldhaus-reisen.de/linienverkehr/linienverkehr.html")
    },
)