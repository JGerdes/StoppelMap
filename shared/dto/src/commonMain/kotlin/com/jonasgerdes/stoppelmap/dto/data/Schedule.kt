package com.jonasgerdes.stoppelmap.dto.data

import com.jonasgerdes.stoppelmap.dto.Localized
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val event: List<Event>
)

typealias EventSlug = String

@Serializable
data class Event(
    val slug: EventSlug,
    val name: String,
    val start: LocalDateTime,
    val end: LocalDateTime? = null,
    val location: MapEntitySlug? = null,
    val description: Localized<String>? = null,
    val artists: List<Artist> = emptyList(),
    val isOfficial: Boolean
)

typealias ArtistSlug = String

@Serializable
data class Artist(
    val slug: ArtistSlug,
    val name: String,
    val type: ArtistType = ArtistType.Unknown
)

enum class ArtistType {
    Unknown,
    Band,
    DJ,
    GuestOfHonor,
}