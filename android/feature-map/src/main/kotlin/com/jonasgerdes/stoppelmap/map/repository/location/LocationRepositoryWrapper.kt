package com.jonasgerdes.stoppelmap.map.repository.location

import android.location.Location
import com.google.android.gms.location.LocationRequest
import com.jonasgerdes.stoppelmap.settings.data.LocationOverride
import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@OptIn(ExperimentalCoroutinesApi::class)
class LocationRepositoryWrapper(
    private val locationRepoMap: Map<LocationOverride, LocationRepository>,
    private val settingRepository: SettingsRepository,
    scope: CoroutineScope,
) : LocationRepository {

    private var currentRepository: LocationRepository = default

    init {
        settingRepository.getSettings()
            .onEach {
                currentRepository =
                    locationRepoMap.getOrDefault(it.locationOverride, default)
            }
            .launchIn(scope)
    }

    override suspend fun getLastKnownLocation(): Location? {
        Timber.d("getLastKnownLocation() with $currentRepository")
        return currentRepository.getLastKnownLocation()
    }

    override fun getLocationUpdates(locationRequest: LocationRequest): Flow<Location> {
        Timber.d("getLocationUpdates() with $currentRepository")
        return settingRepository.getSettings().flatMapLatest {
            locationRepoMap.getOrDefault(it.locationOverride, default)
                .getLocationUpdates(locationRequest)
        }
    }

    private val default get() = locationRepoMap.entries.first().value
}
