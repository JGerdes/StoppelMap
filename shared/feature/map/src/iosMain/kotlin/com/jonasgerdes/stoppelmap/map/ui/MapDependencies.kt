package com.jonasgerdes.stoppelmap.map.ui

import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.usecase.GetMapFilePathUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchMapUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MapDependencies : KoinComponent {
    val getMapFilePathUseCase: GetMapFilePathUseCase by inject()
    val searchMapUseCase: SearchMapUseCase by inject()
    val mapEntityRepository: MapEntityRepository by inject()
}