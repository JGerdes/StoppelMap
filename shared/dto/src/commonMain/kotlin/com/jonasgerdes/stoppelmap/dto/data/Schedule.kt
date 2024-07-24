package com.jonasgerdes.stoppelmap.dto.data

import com.jonasgerdes.stoppelmap.dto.Localized
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class Schedule(
    val events: List<Event>,
    val isWorkInProgress: Boolean,
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
    val participants: List<Participant> = emptyList(),
    val tags: List<TagSlug> = emptyList(),
    val websites: List<Website> = emptyList(),
    val isOfficial: Boolean
)

typealias PersonSlug = String

@Serializable
data class Person(
    val slug: PersonSlug,
    val name: String,
    val images: List<Image>,
    val description: Localized<String>? = null,
)

@Serializable
data class Participant(
    val person: PersonSlug,
    val type: ParticipantType?,
)

enum class ParticipantType {
    Unknown,
    Band,
    DJ,
    GuestOfHonor,
    Speaker
}