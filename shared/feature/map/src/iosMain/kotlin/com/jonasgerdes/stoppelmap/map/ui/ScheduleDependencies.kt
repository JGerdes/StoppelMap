package com.jonasgerdes.stoppelmap.map.ui

import com.jonasgerdes.stoppelmap.map.usecase.GetMapFilePathUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MapDependencies : KoinComponent {
    val getMapFilePathUseCase: GetMapFilePathUseCase by inject()
}