package com.jonasgerdes.stoppelmap.home.usescase

import com.jonasgerdes.stoppelmap.core.domain.DateTimeProvider
import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import org.threeten.bp.*
import javax.inject.Inject


@FlowPreview
class GetFireworkCountdownUseCase @Inject constructor(
    private val currentDateTime: DateTimeProvider,
    private val globalInfoProvider: GlobalInfoProvider
) {

    operator fun invoke(year: Int? = null): CountdownResult {
        val now = currentDateTime()
        val nextDate = LocalDateTime.of(
            2020,
            Month.SEPTEMBER,
            10,
            14,
            0,
            0
        ).atOffset(ZoneOffset.ofHours(2))

        val isSameDay = if (nextDate != null) {
            val nowInstant = now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            val nextInstant = nextDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
            nowInstant == nextInstant
        } else {
            false
        }


        val durations = Duration.between(now, nextDate)
        return when {
            !durations.isNegative -> CountdownResult.Countdown(durations)
            isSameDay -> CountdownResult.Today
            else -> CountdownResult.NoNextDateFound
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
        object Today : CountdownResult()
        data class Countdown(val timeLeft: Duration) : CountdownResult()
    }
}