package com.jonasgerdes.stoppelmap.widget.countdown

import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CountdownWidgetDependencies : KoinComponent {
    val getOpeningCountDownUseCase: GetOpeningCountDownUseCase by inject()
}