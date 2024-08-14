package com.jonasgerdes.stoppelmap.map.location

import co.touchlab.kermit.Logger
import com.jonasgerdes.stoppelmap.map.model.PermissionState
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class IosPermissionRepository(
    private val locationAndPermissionService: LocationAndPermissionService
) : PermissionRepository {
    override fun getLocationPermissionState(): Flow<PermissionState> = callbackFlow {
        Logger.d { "🛡️getLocationPermissionState - build callBackFlow" }
        locationAndPermissionService.permissionCallback = {
            Logger.d { "🛡️getLocationPermissionState - got permission, trySend it" }
            trySend(it)
        }
        awaitClose {
            Logger.d { "🛡️getLocationPermissionState - flow closed, resetting callback" }
            locationAndPermissionService.permissionCallback = null
        }
    }

    override fun requestLocationPermission() {
        locationAndPermissionService.requestLocationPermission()
    }
}