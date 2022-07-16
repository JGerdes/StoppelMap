package com.jonasgerdes.stoppelmap.countdown

import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownFlowUseCase
import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownUseCase
import com.jonasgerdes.stoppelmap.countdown.widget.heart.GingerbreadWidgetSettings
import org.koin.dsl.module


val countdownModule = module {

    single { GingerbreadWidgetSettings(context = get()) }

    factory { GetOpeningCountDownUseCase() }
    factory { GetOpeningCountDownFlowUseCase(getOpeningCountDown = get()) }
}
