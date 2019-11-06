package com.jonasgerdes.stoppelmap.home.usescase

import com.jonasgerdes.stoppelmap.core.domain.DateTimeProvider
import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.threeten.bp.Duration
import javax.inject.Inject
import javax.inject.Singleton


@FlowPreview
class GetCountdownUseCase @Inject constructor(
    private val currentDateTime: DateTimeProvider,
    private val globalInfoProvider: GlobalInfoProvider
) {

    operator fun invoke(year: Int? = null): CountdownResult {
        val now = currentDateTime()
        val nextDate = globalInfoProvider.getSeasons().asSequence()
            .map { it.start }
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