package com.jonasgerdes.stoppelmap.map.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class PermissionRepository(
    private val context: Context,
) {

    private val locationPermissionGranted = MutableStateFlow(false)

    fun getLocationPermissionState(): Flow<Boolean> =
        locationPermissionGranted.also { update() }


    fun update() {
        locationPermissionGranted.value = isLocationPermissionGranted()
    }

    fun isLocationPermissionGranted() =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
}
