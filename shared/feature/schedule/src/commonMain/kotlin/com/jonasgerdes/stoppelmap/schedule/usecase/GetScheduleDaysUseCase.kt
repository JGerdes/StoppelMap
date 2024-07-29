package com.jonasgerdes.stoppelmap.schedule.usecase

import com.jonasgerdes.stoppelmap.schedule.model.Event
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleDay
import com.jonasgerdes.stoppelmap.schedule.model.ScheduleTime
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetScheduleDaysUseCase(
    private val eventRepository: EventRepository,
) {
    operator fun invoke(): Flow<List<ScheduleDay>> =
        eventRepository.getAllEvents().map { events ->
            events.groupBy { it.start.date }
                .entries
                .map { (date, events) ->
                    ScheduleDay(
                        date = date,
                        timeSlots = events
                            .groupBy { it.start.time }
                            .entries
                            .map { (time, events) ->
                                ScheduleTime(
                                    time = time,
                                    events = events.sortedWith(eventLocationComparator)
                                )
                            }
                            .sortedBy { it.time }
                    )
                }
                .sortedBy { it.date }
        }

    private val eventLocationComparator = Comparator<Event> { first, second ->
        when {
            first.locationSlug == second.locationSlug -> 0
            first.locationSlug == null -> -1
            second.locationSlug == null -> 1
            else -> first.locationSlug.compareTo(second.locationSlug)
        }
    }
}