package com.jonasgerdes.stoppelmap.countdown

import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownFlowUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownStateUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.IsCurrentYearsSeasonJustOverUseCase
import org.koin.dsl.module

val countDownModule = module {
    factory {
        IsCurrentYearsSeasonJustOverUseCase(
            clockProvider = get(),
            seasonProvider = get(),
        )
    }
    factory { GetOpeningCountDownFlowUseCase(getOpeningCountDown = get()) }
    factory {
        GetOpeningCountDownUseCase(
            clockProvider = get(),
            seasonProvider = get(),
        )
    }
    factory {
        GetOpeningCountDownStateUseCase(
            getOpeningCountDown = get(),
            isCurrentYearsSeasonJustOver = get(),
        )
    }
}