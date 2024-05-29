package com.jonasgerdes.stoppelmap.countdown.usecase

import com.jonasgerdes.stoppelmap.countdown.model.CountDown
import com.jonasgerdes.stoppelmap.countdown.model.CountDownState
import kotlinx.coroutines.flow.map

class GetOpeningCountDownStateUseCase
internal constructor(
    private val getOpeningCountDown: GetOpeningCountDownFlowUseCase,
    private val isCurrentYearsSeasonJustOver: IsCurrentYearsSeasonJustOverUseCase,
) {

    operator fun invoke() = getOpeningCountDown().map { countDownResult ->
        if (countDownResult is CountDown.InFuture) {
            CountDownState.CountingDown(
                daysLeft = countDownResult.daysLeft,
                hoursLeft = countDownResult.hoursLeft,
                minutesLeft = countDownResult.minutesLeft,
                secondsLeft = countDownResult.secondsLeft,
                season = countDownResult.season,
                showCurrentSeasonIsOverHint = isCurrentYearsSeasonJustOver()
            )
        } else {
            CountDownState.Over
        }
    }
}