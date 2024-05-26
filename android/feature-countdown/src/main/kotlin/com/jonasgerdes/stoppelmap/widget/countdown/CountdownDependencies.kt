package com.jonasgerdes.stoppelmap.widget.countdown

import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownUseCase
import com.jonasgerdes.stoppelmap.widget.CreateStartAppIntentUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CountdownWidgetDependencies : KoinComponent {
    val getOpeningCountDownUseCase: GetOpeningCountDownUseCase by inject()
    val createStartAppIntentUseCase: CreateStartAppIntentUseCase by inject()
}