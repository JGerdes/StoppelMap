package com.jonasgerdes.stoppelmap.map.repository

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.jonasgerdes.stoppelmap.map.location.PermissionRepository
import com.jonasgerdes.stoppelmap.map.model.PermissionState
import com.jonasgerdes.stoppelmap.map.model.PermissionState.Denied
import com.jonasgerdes.stoppelmap.map.model.PermissionState.Granted
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AndroidPermissionRepository(
    private val context: Context,
) : PermissionRepository {

    private val locationPermissionGranted = MutableStateFlow(isLocationPermissionGranted())

    override fun getLocationPermissionState(): Flow<PermissionState> =
        locationPermissionGranted.also { update() }.map { it.toPermissionState() }


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


    private fun Boolean.toPermissionState() = if (this) Granted else Denied
}
