package com.jonasgerdes.stoppelmap.model.entity

fun getNameForItem(item: String) = when (item) {
    "item_beer" -> "Bier"
    "item_softdrinks" -> "Softdrinks"
    "item_shots" -> "Shots"
    "item_longdrinks" -> "Longdrinks"
    "item_cocktails" -> "Cocktails"
    "item_punchbowle" -> "Bowle"
    "item_wine" -> "Wein"
    "item_pizza" -> "Pizza"
    "item_fries" -> "Pommes"
    "item_sausauge" -> "Bratwurst"
    "item_currywurst" -> "Currywurst"
    "item_hotdog" -> "HotDog"
    "item_hamburger" -> "Hamburger"
    "item_corn" -> "Mais"
    "item_mushrooms" -> "Pilze"
    "item_fish" -> "Fisch"
    "item_salmon" -> "Lachs"
    "item_steak" -> "Steak"
    "item_bread" -> "Brote"
    "item_beacon" -> "Schinken"
    "item_pretzel" -> "Brezel"
    "item_churros" -> "Churros"
    "item_broccoli" -> "Brokkoli"
    "item_pasta" -> "Pasta"
    "item_fried_potatoes" -> "Bratkartoffeln"
    "item_fried_egg" -> "Spiegelei"
    "item_potato_fritter" -> "Reibekuchen"
    "item_potatoes" -> "Kartoffeln"
    "item_chinese" -> "Chinesisch"
    "item_spit_roast" -> "Spießbraten"
    "item_gyros" -> "Gyros"
    "item_cheese" -> "Käse"
    "item_tarte_flambee" -> "Flammkuchen"

    "item_crepe" -> "Crepé"
    "item_roasted_almonds" -> "Mandeln"
    "item_ice_cream" -> "Eis"
    "item_slush" -> "Slusheis"
    "item_frozen_yogurt" -> "Frozen Yogurt"
    "item_cotton_Candy" -> "Zuckerwatte"
    "item_gingerbread" -> "Lebkuchen"
    "item_waffle" -> "Waffeln"
    "item_wine_gums" -> "Weingummi"
    "item_bonbons" -> "Bonbon"
    "item_poffertje" -> "Poffertje"
    "item_backery" -> "Backwaren"
    "item_chocolate_fruits" -> "Schokofrüchte"

    "item_atm" -> "Geldautomat"

    "game_cross_bow" -> "Armbrust"
    "game_machine" -> "Automat"
    "game_basketball" -> "Basketball"
    "game_archery" -> "Bogenschießen"
    "game_darts" -> "Dart"
    "game_can_knockdown" -> "Dosenwerfen"
    "game_duck_pond" -> "Ententeich"
    "game_pull_strings" -> "Fädenziehen"
    "game_football" -> "Fußball"
    "game_claw_crane" -> "Greifarm"
    "game_high_striker" -> "Hau-den-Lukas"
    "game_lottery" -> "Losbude"
    "game_horse_race" -> "Pferderennen"
    "game_ring_toss" -> "Ringewerfen"
    "game_shooting" -> "Schießbude"
    "game_skee_ball" -> "Skee-Ball"

    else -> throw RuntimeException("no name for item $item")
}

fun getNamesForType(type: String): List<String> {
    val names = getNameForType(type)
    return when (names) {
        is String -> listOf(names)
        is List<*> -> names as List<String>
        else -> listOf(names.toString())
    }
}

@Suppress("IMPLICIT_CAST_TO_ANY")
private fun getNameForType(type: String) = when (type) {
    "for-kids" -> "Für Kinder"
    "marquee" -> "Festzelt"
    "bar" -> "Ausschank"
    "coaster" -> "Achterbahn"
    "swing-ride" -> listOf("Kettenflieger", "Kettenkarusell")
    "funhouse" -> "Spaßhaus"
    "bumper-cars" -> "Autoscooter"
    "twister" -> "Twister"
    "marry-go-round" -> "Karusell"
    "log-flume" -> listOf("Baumstammkanal", "Wasserbahn")
    "live-show" -> "Show"
    "ghost-train" -> "Geisterbahn"
    "restroom" -> listOf("Toilette", "WC", "Klo")
    "accessible_restroom" -> listOf("Barrierefreie Toilette", "Barrierefreies WC",
            "Rollstuhlgerechte Toilette")
    "mens_restroom" -> listOf("Herren-Toilette", "Herren-WC")
    "womens_restroom" -> listOf("Damen-Toilette", "Damen-WC")
    else -> throw RuntimeException("no name for item $type")
}