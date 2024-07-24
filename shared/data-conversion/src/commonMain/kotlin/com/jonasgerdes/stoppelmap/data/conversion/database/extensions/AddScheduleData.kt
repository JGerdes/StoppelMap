package com.jonasgerdes.stoppelmap.data.conversion.database.extensions

import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.data.schedule.Event
import com.jonasgerdes.stoppelmap.data.schedule.ParticipantType
import com.jonasgerdes.stoppelmap.dto.data.Schedule

internal fun StoppelMapDatabase.addScheduleData(schedule: Schedule) {
    schedule.events.forEach { event ->
        eventQueries.insert(
            Event(
                slug = event.slug,
                name = event.name,
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
                    com.jonasgerdes.stoppelmap.dto.data.ParticipantType.Unknown -> ParticipantType.Unknown
                    com.jonasgerdes.stoppelmap.dto.data.ParticipantType.Band -> ParticipantType.Band
                    com.jonasgerdes.stoppelmap.dto.data.ParticipantType.DJ -> ParticipantType.DJ
                    com.jonasgerdes.stoppelmap.dto.data.ParticipantType.GuestOfHonor -> ParticipantType.GuestOfHonor
                    com.jonasgerdes.stoppelmap.dto.data.ParticipantType.Speaker -> ParticipantType.Speaker
                    null -> null
                }
            )
        }
        addWebsites(event.slug, event.websites)
    }
}