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
    val name: String,
    val type: MapEntityType,
    val subType: SubTypeSlug? = null,
    val operator: Operator? = null,
    val aliases: List<Localized<String>>? = null,
    val description: Localized<String>? = null,
    val center: Location,
    val offers: List<Offer> = emptyList(),
    val tags: List<TagSlug> = emptyList(),
    val services: List<ServiceSlug> = emptyList(),
    val admissionFees: List<Fee> = emptyList(),
    val images: List<Image> = emptyList(),
    val websites: List<String> = emptyList(),
    val isSearchable: Boolean,
)

enum class MapEntityType(val id: String) {
    Misc("misc"),
    Bar("bar"),
    Building("building"),
    CandyStall("candy_stall"),
    Entrance("entrance"),
    Expo("expo"),
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
    Taxi("taxi"),
}

typealias SubTypeSlug = String

@Serializable
data class SubType(
    val slug: SubTypeSlug,
    val name: Localized<String>,
    val aliases: List<Localized<String>>? = null,
)