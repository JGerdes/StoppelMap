package com.jonasgerdes.stoppelmap.map.ui

import com.jonasgerdes.stoppelmap.map.data.DeeplinkRepository
import com.jonasgerdes.stoppelmap.map.data.MapEntityRepository
import com.jonasgerdes.stoppelmap.map.location.LocationRepository
import com.jonasgerdes.stoppelmap.map.location.PermissionRepository
import com.jonasgerdes.stoppelmap.map.usecase.GetMapFilePathUseCase
import com.jonasgerdes.stoppelmap.map.usecase.GetQuickSearchSuggestionsUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchMapUseCase
import com.jonasgerdes.stoppelmap.schedule.repository.EventRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MapDependencies : KoinComponent {

    val deeplinkRepository: DeeplinkRepository by inject()
    val getMapFilePathUseCase: GetMapFilePathUseCase by inject()
    val searchMapUseCase: SearchMapUseCase by inject()
    val mapEntityRepository: MapEntityRepository by inject()
    val locationRepository: LocationRepository by inject()
    val permissionRepository: PermissionRepository by inject()

    val eventRepository: EventRepository by inject()
    val getQuickSearchSuggestionsUseCase: GetQuickSearchSuggestionsUseCase by inject()
}