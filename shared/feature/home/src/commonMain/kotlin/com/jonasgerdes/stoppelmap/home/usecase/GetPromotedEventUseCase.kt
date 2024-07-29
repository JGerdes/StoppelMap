package com.jonasgerdes.stoppelmap.home.usecase

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.schedule.model.Event
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GetPromotedEventsUseCase(
    private val eventRepository: EventRepository,
    private val clockProvider: ClockProvider,
) {
    operator fun invoke(): Flow<List<Event>> {
        val now = clockProvider.nowAsInstant()
        // Simulating us being in past so we still see events 15 after they're started.
        val adjustedNow =
            clockProvider.toLocalDateTime(now.minus(15.toDuration(DurationUnit.MINUTES)))
        return combine(
            eventRepository.getBookmarkedEvents(adjustedNow),
            eventRepository.getAllOfficialEvents(adjustedNow)
        ) { bookmarkedEvents, officialEvents ->

            fun Event.isDuringNext24Hours() = clockProvider.toInstant(start) - now < 24.hours

            listOf(
                bookmarkedEvents.filter { it.isDuringNext24Hours() }.take(3),
                officialEvents.filter { it.isDuringNext24Hours() }.take(1)
            ).flatten()
                .distinctBy { it.slug } // Don't show bookmarked official events twice
                .sortedBy { it.start }
        }
    }
}