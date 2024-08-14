package com.jonasgerdes.stoppelmap.map.location

import com.jonasgerdes.stoppelmap.map.model.PermissionState
import com.jonasgerdes.stoppelmap.map.model.PermissionState.Denied
import com.jonasgerdes.stoppelmap.map.model.PermissionState.Granted
import com.jonasgerdes.stoppelmap.map.model.PermissionState.NotDetermined
import com.jonasgerdes.stoppelmap.map.model.SensorLocation
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.CoreLocation.CLActivityTypeOtherNavigation
import platform.CoreLocation.CLAuthorizationStatus
import platform.CoreLocation.CLLocation
import platform.CoreLocation.CLLocationManager
import platform.CoreLocation.CLLocationManagerDelegateProtocol
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedAlways
import platform.CoreLocation.kCLAuthorizationStatusAuthorizedWhenInUse
import platform.CoreLocation.kCLAuthorizationStatusDenied
import platform.CoreLocation.kCLAuthorizationStatusNotDetermined
import platform.CoreLocation.kCLAuthorizationStatusRestricted
import platform.darwin.NSObject

// TODO: Inject the usual way once https://github.com/InsertKoinIO/koin/issues/1492 is resolved
object LocationAndPermissionDependencies {
    val locationAndPermissionService = LocationAndPermissionService(locationManager = CLLocationManager())
}

@OptIn(ExperimentalForeignApi::class)
class LocationAndPermissionService(
    private val locationManager: CLLocationManager
) : CLLocationManagerDelegateProtocol, NSObject() {

    var locationCallback: ((SensorLocation) -> Unit)? = null
    var permissionCallback: ((PermissionState) -> Unit)? = null

    init {
        locationManager.setDelegate(this)
    }


    private fun checkPermission(status: CLAuthorizationStatus = locationManager.authorizationStatus): PermissionState {
        return when (status) {
            kCLAuthorizationStatusAuthorizedAlways -> Granted
            kCLAuthorizationStatusAuthorizedWhenInUse -> Granted
            kCLAuthorizationStatusDenied -> Denied
            kCLAuthorizationStatusNotDetermined -> NotDetermined
            kCLAuthorizationStatusRestricted -> Denied
            else -> Denied
        }
    }

    fun requestLocationPermission() {
        locationManager.requestWhenInUseAuthorization()
    }

    fun startLocationUpdates() {
        with(locationManager) {
            setPausesLocationUpdatesAutomatically(true)
            setAllowsBackgroundLocationUpdates(false)
            setActivityType(CLActivityTypeOtherNavigation)
            startUpdatingLocation()
        }
    }

    fun stopLocationUpdates() {
        locationManager.stopUpdatingLocation()
    }

    fun getLastKnownLocation(): SensorLocation? {
        return locationManager.location?.toSensorLocation()
    }

    override fun locationManager(manager: CLLocationManager, didChangeAuthorizationStatus: CLAuthorizationStatus) {
        val state = checkPermission(didChangeAuthorizationStatus)
        permissionCallback?.invoke(state)
    }

    override fun locationManager(
        manager: CLLocationManager,
        didUpdateToLocation: CLLocation,
        fromLocation: CLLocation
    ) {
        locationCallback?.invoke(didUpdateToLocation.toSensorLocation())
    }


    private fun CLLocation.toSensorLocation(): SensorLocation {
        return coordinate.useContents {
            SensorLocation(
                latitude = latitude,
                longitude = longitude,
                accuracyMeters = horizontalAccuracy.toFloat()
            )
        }
    }
}