package com.jonasgerdes.stoppelmap.map.location

import kotlinx.coroutines.flow.Flow

interface PermissionRepository {

    fun hasLocationPermission(): Flow<Boolean>
}