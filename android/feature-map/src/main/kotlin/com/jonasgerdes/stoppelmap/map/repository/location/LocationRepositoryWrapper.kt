package com.jonasgerdes.stoppelmap.map.repository.location

import com.jonasgerdes.stoppelmap.map.location.LocationRepository
import com.jonasgerdes.stoppelmap.map.model.SensorLocation
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

    override suspend fun getLastKnownLocation(): SensorLocation? {
        Timber.d("getLastKnownLocation() with $currentRepository")
        return currentRepository.getLastKnownLocation()
    }

    override fun getLocationUpdates(): Flow<SensorLocation> {
        Timber.d("getLocationUpdates() with $currentRepository")
        return settingRepository.getSettings().flatMapLatest {
            locationRepoMap.getOrDefault(it.locationOverride, default).getLocationUpdates()
        }
    }

    private val default get() = locationRepoMap.entries.first().value
}
