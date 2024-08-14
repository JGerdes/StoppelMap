package com.jonasgerdes.stoppelmap.map.location

import com.jonasgerdes.stoppelmap.map.model.PermissionState
import kotlinx.coroutines.flow.Flow

interface PermissionRepository {

    fun getLocationPermissionState(): Flow<PermissionState>

    fun requestLocationPermission() = Unit
}