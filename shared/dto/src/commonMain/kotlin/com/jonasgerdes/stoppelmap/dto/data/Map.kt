package com.jonasgerdes.stoppelmap.dto.data

import com.jonasgerdes.stoppelmap.dto.Localized
import kotlinx.serialization.Serializable

@Serializable
data class Map(
    val entities: List<MapEntity>,
    val isWorkInProgress: Boolean,
)

typealias MapEntitySlug = String

@Serializable
data class MapEntity(
    val slug: MapEntitySlug,
    val name: String?,
    val type: MapEntityType,
    val subType: SubTypeSlug? = null,
    val operator: OperatorSlug? = null,
    val aliases: List<Alias> = emptyList(),
    val description: Localized<String>? = null,
    val center: Location,
    val bbox: BoundingBox,
    val priority: Int,
    val tags: List<TagSlug> = emptyList(),
    val offers: List<Offer> = emptyList(),
    val services: List<ServiceSlug> = emptyList(),
    val admissionFees: List<Fee> = emptyList(),
    val images: List<Image> = emptyList(),
    val websites: List<Website> = emptyList(),
    val isSearchable: Boolean,
)

enum class MapEntityType(val id: String) {
    Misc("misc"),
    Bar("bar"),
    Building("building"),
    CandyStall("candy_stall"),
    Entrance("entrance"),
    Exhibition("exhibition"),
    FoodStall("food_stall"),
    GameStall("game_stall"),
    Info("info"),
    Parking("parking"),
    Platform("platform"),
    Restaurant("restaurant"),
    Restroom("restroom"),
    Ride("ride"),
    SellerStall("seller_stall"),
    Station("station"),
    Taxi("taxi");

    companion object {
        fun fromId(id: String) = MapEntityType.entries
            .firstOrNull { it.id == id }
    }
}

typealias SubTypeSlug = String

@Serializable
data class SubType(
    val slug: SubTypeSlug,
    val name: Localized<String>,
    val aliases: List<Alias>? = null,
)

@Serializable
data class BoundingBox(
    val southLat: Double,
    val westLng: Double,
    val northLat: Double,
    val eastLng: Double,
)