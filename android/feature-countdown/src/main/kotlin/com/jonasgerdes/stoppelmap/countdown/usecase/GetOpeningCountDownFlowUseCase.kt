package com.jonasgerdes.stoppelmap.countdown.usecase

import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow

private const val UPDATE_INTERVAL_MS = 1_000L

class GetOpeningCountDownFlowUseCase
internal constructor(
    private val getOpeningCountDown: GetOpeningCountDownUseCase
) {
    operator fun invoke() = flow {
        var result: CountDown
        do {
            result = getOpeningCountDown()
            emit(result)
            delay(UPDATE_INTERVAL_MS)
        } while (result is CountDown.InFuture)
    }

}
