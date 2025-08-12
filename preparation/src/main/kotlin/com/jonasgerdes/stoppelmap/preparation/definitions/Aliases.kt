package com.jonasgerdes.stoppelmap.preparation.definitions

import com.jonasgerdes.stoppelmap.dto.Locales.de
import com.jonasgerdes.stoppelmap.dto.Locales.en
import com.jonasgerdes.stoppelmap.dto.data.Alias
import com.jonasgerdes.stoppelmap.dto.data.MapEntityType
import com.jonasgerdes.stoppelmap.dto.data.TypeAliases

val typeAliases = MapEntityType.entries.mapNotNull { type ->
    val aliases = when (type) {
        MapEntityType.Misc -> null
        MapEntityType.Bar -> listOf(
            Alias("Ausschank", de),
            Alias("Bar", en)
        )

        MapEntityType.Building -> null
        MapEntityType.CandyStall -> listOf(
            Alias("Süßwaren", de),
            Alias("Candy stall", en)
        )

        MapEntityType.Entrance -> null
        MapEntityType.Exhibition -> listOf(
            Alias("Gewerbestand", de),
            Alias("Gewerbeschau", de)
        )

        MapEntityType.FoodStall -> listOf(
            Alias("Imbissstand", de),
            Alias("Food stall", en),
            Alias("Snack stall", en),
        )

        MapEntityType.GameStall -> listOf(
            Alias("Spielbude", de),
            Alias("Game stall", en)
        )

        MapEntityType.Info -> listOf(
            Alias("Information"),
        )

        MapEntityType.Parking -> listOf(
            Alias("Parkplatz", de),
            Alias("Parking", en)
        )

        MapEntityType.Platform -> null
        MapEntityType.Restaurant -> null
        MapEntityType.Restroom -> listOf(
            Alias("Toilette", de),
            Alias("Klo", de),
            Alias("Restroom", en),
            Alias("WC"),
        )

        MapEntityType.Ride -> listOf(
            Alias("Fahrgeschäft", de),
            Alias("Fun ride", en)
        )

        MapEntityType.SellerStall -> listOf(
            Alias("Verkaufsstand", de),
            Alias("Seller Stall", en)
        )

        MapEntityType.Station -> null
        MapEntityType.Taxi -> listOf(
            Alias("Taxi")
        )

        MapEntityType.RedCross -> null
        MapEntityType.Police -> null
        MapEntityType.Atm -> null
        MapEntityType.WaterFountain -> null
        MapEntityType.TransitTickets -> null
        MapEntityType.Bike -> listOf(
            Alias("Fahrradparkplatz", de),
            Alias("Bike parking", en),
        )
    }
    aliases?.let { TypeAliases(type, it) }
}