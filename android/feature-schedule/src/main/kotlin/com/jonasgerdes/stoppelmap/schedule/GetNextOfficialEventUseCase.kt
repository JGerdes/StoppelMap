package com.jonasgerdes.stoppelmap.schedule

import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.data.Event
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GetNextOfficialEventUseCase(
    private val eventRepository: EventRepository,
    private val clockProvider: ClockProvider,
) {

    operator fun invoke() = flow {
        var isDone = false
        while (!isDone) {
            val now = clockProvider.nowAsInstant()
            // Simulating us being in past so we still see events 15 after they're started.
            val adjustedNow =
                clockProvider.toLocalDateTime(now.minus(15.toDuration(DurationUnit.MINUTES)))
            val nextEvent = eventRepository.getAllOfficialEvents()
                .filter { it.start > adjustedNow }
                .minByOrNull { it.start }
                ?.takeIf {
                    clockProvider.toInstant(it.start) - now < 24.toDuration(DurationUnit.HOURS)
                }

            if (nextEvent == null) {
                emit(Result.None)
                isDone = true
            } else {
                emit(Result.Some(nextEvent))
                delay(5.toDuration(DurationUnit.MINUTES))
            }
        }
    }


    sealed interface Result {
        object None : Result
        data class Some(val event: Event) : Result
    }
}
