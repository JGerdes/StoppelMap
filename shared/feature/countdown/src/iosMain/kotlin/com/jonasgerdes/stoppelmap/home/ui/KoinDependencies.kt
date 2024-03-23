package com.jonasgerdes.stoppelmap.home.ui

import com.jonasgerdes.stoppelmap.countdown.usecase.GetOpeningCountDownStateUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeDependencies : KoinComponent {
    val getCountDownStateUseCase: GetOpeningCountDownStateUseCase by inject()
}