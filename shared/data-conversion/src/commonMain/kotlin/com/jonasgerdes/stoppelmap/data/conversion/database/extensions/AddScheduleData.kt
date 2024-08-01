package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.schedule.Event
import com.jonasgerdes.stoppelmap.data.schedule.ParticipantType
import com.jonasgerdes.stoppelmap.dto.data.Schedule
import com.jonasgerdes.stoppelmap.dto.data.ParticipantType as DtoParticipantType

internal fun StoppelMapDatabase.addScheduleData(schedule: Schedule) {
    schedule.events.forEach { event ->
        eventQueries.insert(
            Event(
                slug = event.slug,
                nameKey = addLocalizedString(event.name, event.slug, "name"),
                start = event.start,
                end = event.end,
                descriptionKey = event.description?.let {
                    addLocalizedString(it, event.slug, "description")
                },
                locationSlug = event.location,
                isOfficial = event.isOfficial
            )
        )
        event.participants.forEach {
            event_personQueries.insert(
                eventSlug = event.slug,
                personSlug = it.person,
                type = when (it.type) {
                    DtoParticipantType.Unknown -> ParticipantType.Unknown
                    DtoParticipantType.Band -> ParticipantType.Band
                    DtoParticipantType.DJ -> ParticipantType.DJ
                    DtoParticipantType.GuestOfHonor -> ParticipantType.GuestOfHonor
                    DtoParticipantType.Speaker -> ParticipantType.Speaker
                    null -> null
                }
            )
        }
        addWebsites(event.slug, event.websites)
        event.tags.forEach {
            event_tagQueries.insert(event.slug, it)
        }
    }
}