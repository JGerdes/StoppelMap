package com.jonasgerdes.stoppelmap.map.usecase

import com.jonasgerdes.stoppelmap.core.domain.GlobalInfoProvider

class IsUserInAreaUseCase(
    private val globalInfoProvider: GlobalInfoProvider
) {
    operator fun invoke(userLocation: GetUserLocationUseCase.Location?): UserState {
        val area = globalInfoProvider.getAreaBounds()
        return when {
            userLocation == null -> UserState.UNDEFINED
            userLocation.latitude < area.southLatitude -> UserState.OUTSIDE_AREA
            userLocation.latitude > area.northLatitude -> UserState.OUTSIDE_AREA
            userLocation.longitude < area.westLongitude -> UserState.OUTSIDE_AREA
            userLocation.longitude > area.eastLongitude -> UserState.OUTSIDE_AREA
            else -> UserState.IN_AREA
        }

    }

    enum class UserState {
        IN_AREA,
        OUTSIDE_AREA,
        UNDEFINED
    }

}