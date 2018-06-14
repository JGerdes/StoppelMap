package com.jonasgerdes.stoppelmap.model.entity

import java.util.*

data class Stall(
        val slug: String,
        val type: String,
        val centerLng: Double? = null,
        val centerLat: Double? = null,
        val minLng: Double? = null,
        val maxLng: Double? = null,
        val minLat: Double? = null,
        val maxLat: Double? = null,
        val name: String? = null,
        val operator: String? = null,
        val description: String? = null,
        val priority: Int = 0,
        val isTent: Boolean = false,
        val isSearchable: Boolean = true,
        val isForKids: Boolean = false
)

data class Alias(val stall: String, val alias: String)
data class Image(
        val stall: String,
        val file: String,
        val type: String,
        val author: String? = null,
        val license: String? = null
)

data class Phone(
        val stall: String,
        val name: String,
        val number: String,
        val numberReadable: String
)

data class Restroom(
        val stall: String,
        val accessible: Boolean,
        val forWomen: Boolean,
        val forMen: Boolean
)

data class Item(
        val slug: String,
        val name: String
)

data class StallItem(
        val stall: String,
        val item: String
)

data class SubType(
        val slug: String,
        val name: String
)

data class StallSubType(
        val stall: String,
        val subType: String
)

data class Event(
        val slug: String,
        val name: String,
        val start: Date,
        val end: Date?,
        val location: String? = null,
        val description: String? = null
)


data class Url(
        val event: String,
        val url: String,
        val type: String
)

data class Tag(
        val slug: String,
        val name: String
)

data class EventTag(
        val event: String,
        val tag: String
)

data class Artist(
        val slug: String,
        val name: String
)

data class EventArtist(
        val event: String,
        val artist: String
)

data class Route(
        val slug: String,
        val name: String
)

data class Station(
        val slug: String,
        val name: String,
        val route: String,
        val comment: String? = null,
        val longitude: Double? = null,
        val latitude: Double? = null
)

data class Departure(
        val station:String,
        val time: Date
)

data class TransportPrice(
        val station: String,
        val type: String,
        val price: Int
)
