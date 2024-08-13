package com.jonasgerdes.stoppelmap.map.ui

import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.location.LocationRepository
import com.jonasgerdes.stoppelmap.map.location.PermissionRepository
import com.jonasgerdes.stoppelmap.map.usecase.GetMapFilePathUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchMapUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MapDependencies : KoinComponent {
    val getMapFilePathUseCase: GetMapFilePathUseCase by inject()
    val searchMapUseCase: SearchMapUseCase by inject()
    val mapEntityRepository: MapEntityRepository by inject()
    val locationRepository: LocationRepository by inject()
    val permissionRepository: PermissionRepository by inject()
}