package com.jonasgerdes.stoppelmap.preparation.definitions

import com.jonasgerdes.stoppelmap.dto.Locale
import com.jonasgerdes.stoppelmap.dto.Locales.de
import com.jonasgerdes.stoppelmap.dto.Locales.en
import com.jonasgerdes.stoppelmap.dto.data.Alias
import com.jonasgerdes.stoppelmap.dto.data.SubType
import com.jonasgerdes.stoppelmap.preparation.definitions.SubTypeSlugs.accessibleRestroom
import com.jonasgerdes.stoppelmap.preparation.definitions.SubTypeSlugs.partyTent
import com.jonasgerdes.stoppelmap.preparation.definitions.SubTypeSlugs.urinalRestroom
import com.jonasgerdes.stoppelmap.preparation.definitions.SubTypeSlugs.womensRestroom
import com.jonasgerdes.stoppelmap.preparation.localizedString

object SubTypeSlugs {
    const val partyTent = "party_tent"
    const val accessibleRestroom = "accessible_restroom"
    const val womensRestroom = "womens_restroom"
    const val urinalRestroom = "urinal_restroom"
}

val restroomSubTypes = listOf(
    subType(
        slug = accessibleRestroom,
        de = "Rollstuhlgerechte Toilette",
        en = "Wheelchair accessible restoom",
        de to "Barrierefreie Toilette",
        de to "Barrierefreies WC"
    ),
    subType(slug = womensRestroom, de = "Damentoilette", en = "Women's restroom"),
    subType(slug = urinalRestroom, de = "Urinal", en = "Urinal", de to "Pinkelrinne"),
)

val barSubTypes = listOf(
    subType(slug = partyTent, de = "Festzelt", en = "Party tent"),
)
val rideSubTypes = listOf(
    subType(slug = "coaster", de = "Achterbahn", en = "Roller coaster"),
    subType(slug = "slide", de = "Rutsche", en = "Slide"),
    subType(slug = "swing_ride", de = "Kettenflieger", en = "Swing ride", de to "Kettenkarusell"),
    subType(slug = "fun_house", de = "Spaßhaus", en = "Fun house"),
    subType(slug = "labyrinth", de = "Labyrinth", en = "Labyrinth"),
    subType(slug = "top_spin", de = "Top Spin", en = "Top spin"),
    subType(slug = "bumper_cars", de = "Autoscooter", en = "Bumper cars", en to "Dodgems"),
    subType(slug = "twister", de = "Twister", en = "Twister"),
    subType(slug = "merry_go_round", de = "Karusell", en = "Merry go round"),
    subType(slug = "log_flume", de = "Baumstammkanal", en = "Log flume", de to "Wasserbahn"),
    subType(slug = "live_show", de = "Show", en = "Show"),
    subType(slug = "tunnel_of_horror", de = "Geisterbahn", en = "Tunnel of horror"),
    subType(slug = "break_dancer", de = "Break Dancer", en = "Break Dancer"),
    subType(slug = "ferris_wheel", de = "Riesenrad", en = "Ferris wheel"),
    subType(slug = "flying_carpet", de = "Fliegender Teppich", en = "Flying carpet"),
    subType(slug = "free_fall_tower", de = "Freifallturm", en = "Free fall tower"),
    subType(slug = "x_drive", de = "X-Drive", en = "X-Drive"),
    subType(slug = "booster", de = "Booster", en = "Booster"),
    subType(slug = "jump_n_smile", de = "Jump & Smile", en = "Jump & Smile"),
    subType(slug = "escape", de = "Escape", en = "Escape"),
    subType(slug = "polyp", de = "Polyp", en = "Polyp", de to "Krake", en to "Octopus"),
    subType(slug = "caterpillar", de = "Berg- und Talbahn", en = "Caterpillar", de to "Raupenbahn"),
    subType(slug = "junior_jets", de = "Baby-Flug", en = "Junior Jets"),
    subType(slug = "4d_cinema", de = "4D-Kino", en = "4D Cinema"),
    subType(slug = "junior_bumper_cars", de = "Kinder-Autoscooter", en = "Junior bumper cars"),
    subType(slug = "kiddie_ride", de = "Kinderschleife", en = "Kiddie ride"),
    subType(slug = "top_star", de = "Top Star", en = "Top Star"),
)

val gameSubTypes = listOf(
    subType(slug = "cross_bow", de = "Armbrust", en = "Cross bow"),
    subType(slug = "machines", de = "Automaten", en = "Machines"),
    subType(slug = "basketball", de = "Basketball", en = "Basketball"),
    subType(slug = "archery", de = "Bogenschießen", en = "archery"),
    subType(slug = "darts", de = "Darts", en = "Darts"),
    subType(slug = "can_knockdown", de = "Dosenwerfen", en = "Can knockdown"),
    subType(slug = "duck_pond", de = "Ententeich", en = "Duck pond"),
    subType(slug = "flying_frogs", de = "Fliegende Frösche", en = "Flying frogs"),
    subType(slug = "pull_strings", de = "Fädenziehen", en = "Pull strings"),
    subType(slug = "football", de = "Fußball", en = "football"),
    subType(slug = "claw_crane", de = "Greifer", en = "Claw machine", de to "Greifautomat"),
    subType(slug = "high_striker", de = "Hau-den-Lukas", en = "High striker"),
    subType(slug = "lottery", de = "Losbude", en = "Lottery"),
    subType(slug = "horse_race", de = "Pferderennen", en = "Horse race"),
    subType(slug = "ring_toss", de = "Ringewerfen", en = "Ring toss"),
    subType(slug = "shooting", de = "Schießbude", en = "Shooting gallery", en to "Aunt Sally"),
    subType(slug = "skee_ball", de = "Skee-Ball", en = "Skee-Ball"),
    subType(slug = "nail", de = "Nagelbalken", en = "Hammer"),
)

val subTypes = rideSubTypes + gameSubTypes + restroomSubTypes + barSubTypes

private fun subType(
    slug: String,
    de: String,
    en: String,
    vararg aliases: Pair<Locale, String>
) = SubType(
    slug = slug,
    name = localizedString(de = de, en = en),
    aliases = aliases.map {
        Alias(
            string = it.second,
            locale = it.first,
        )
    }
)