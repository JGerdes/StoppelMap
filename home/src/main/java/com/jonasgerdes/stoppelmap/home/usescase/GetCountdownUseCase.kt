package com.jonasgerdes.stoppelmap.home.usescase

import com.jonasgerdes.stoppelmap.core.domain.DateTimeProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.threeten.bp.Duration

@FlowPreview
class GetCountdownUseCase(
    private val currentDateTime: DateTimeProvider,
    private val getStoppelmarktDates: GetStoppelmarktDatesUseCase
) {

    operator fun invoke(year: Int? = null): CountdownResult {
        val now = currentDateTime().toLocalDateTime()
        val nextDate = getStoppelmarktDates().asSequence()
            .filter { it.isAfter(now) }
            .firstOrNull()

        return when {
            nextDate == null -> CountdownResult.NoNextDateFound
            year != null && nextDate.year != year -> CountdownResult.AlreadyStarted
            else -> CountdownResult.Countdown(Duration.between(now, nextDate))
        }
    }

    fun asFlow(updateInterval: Duration) = flow {
        while (true) {
            emit(invoke())
            delay(updateInterval.toMillis())
        }
    }

    sealed class CountdownResult {
        object NoNextDateFound : CountdownResult()
        object AlreadyStarted : CountdownResult()
        data class Countdown(val timeLeft: Duration) : CountdownResult()
    }
}