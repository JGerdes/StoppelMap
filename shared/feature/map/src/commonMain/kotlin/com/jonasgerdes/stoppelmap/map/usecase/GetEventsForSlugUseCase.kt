package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.map.model.Event
import com.jonasgerdes.stoppelmap.map.model.EventDay
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.flow.first

class GetEventsForMapEntityUseCase(
    private val eventRepository: EventRepository
) {
    suspend operator fun invoke(slug: String): List<EventDay> =
        eventRepository.getAllForLocation(slug).first().map { event ->
            Event(
                slug = event.slug,
                name = event.name,
                start = event.start,
                end = event.end,
                description = event.description,
                isBookmarked = event.isBookmarked,
            )
        }.groupBy { it.start.date }
            .map {
                EventDay(
                    date = it.key,
                    events = it.value
                )
            }.sortedBy { it.date }
}