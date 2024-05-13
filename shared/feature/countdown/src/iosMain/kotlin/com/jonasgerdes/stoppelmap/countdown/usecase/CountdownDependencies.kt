package com.jonasgerdes.stoppelmap.countdown.usecase

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CountdownDependencies : KoinComponent {
    val getOpeningCountDownUseCase: GetOpeningCountDownUseCase by inject()
}