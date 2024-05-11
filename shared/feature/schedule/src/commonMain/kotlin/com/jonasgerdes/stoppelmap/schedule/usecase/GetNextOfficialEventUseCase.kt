package com.jonasgerdes.stoppelmap.schedule.usecase

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.base.contract.ClockProvider
import com.jonasgerdes.stoppelmap.data.Event
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import kotlinx.coroutines.CancellationException
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class GetNextOfficialEventUseCase(
    private val eventRepository: EventRepository,
    private val clockProvider: ClockProvider,
) {

    suspend operator fun invoke(): Result {
        return try {
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
                Result.None
            } else {
                Result.Some(nextEvent)
            }
        } catch (t: Throwable) {
            if (t is CancellationException) throw t
            Logger.w(t) { "GetNextOfficialEvent crashed" }
            Result.None
        }
    }


    sealed interface Result {
        object None : Result
        data class Some(val event: Event) : Result
    }
}
