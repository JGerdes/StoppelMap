package com.jonasgerdes.stoppelmap.map


import android.content.Context
import android.location.Location
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.LocationServices
import com.jonasgerdes.stoppelmap.map.location.LocationRepository
import com.jonasgerdes.stoppelmap.map.location.PermissionRepository
import com.jonasgerdes.stoppelmap.map.repository.AndroidPermissionRepository
import com.jonasgerdes.stoppelmap.map.repository.location.AndroidFusedLocationRepository
import com.jonasgerdes.stoppelmap.map.repository.location.FakeLocationRepository
import com.jonasgerdes.stoppelmap.map.repository.location.LocationRepositoryWrapper
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel
import com.jonasgerdes.stoppelmap.map.usecase.IsLocationInAreaUseCase
import com.jonasgerdes.stoppelmap.settings.data.LocationOverride
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mapUserData")

val androidMapModule = module {

    viewModel {
        MapViewModel(
            getMapFilePath = get(),
            searchMap = get(),
            mapEntityRepository = get(),
            permissionRepository = get(),
            locationRepository = get(),
        )
    }

    single { AndroidPermissionRepository(context = get()) }
    single<PermissionRepository> { get<AndroidPermissionRepository>() }

    single { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    single<LocationRepository> {
        LocationRepositoryWrapper(
            locationRepoMap = mapOf(
                LocationOverride.None to AndroidFusedLocationRepository(
                    fusedLocationProviderClient = get()
                ),
                LocationOverride.Amtmannsbult to FakeLocationRepository(
                    Location("FakeProvider").apply {
                        longitude = 8.295414
                        latitude = 52.748587
                    }
                ),
                LocationOverride.BusToEntrance to FakeLocationRepository(
                    startLocation = Location("FakeProvider").apply {
                        longitude = 8.298956
                        latitude = 52.743931
                        bearing = 90f
                        accuracy = 20f
                    },
                    endLocation = Location("FakeProvider").apply {
                        longitude = 8.298445
                        latitude = 52.747623
                        bearing = 180f
                        accuracy = 2f
                    },
                )
            ),
            settingRepository = get(),
            scope = get()
        )
    }


    factory { IsLocationInAreaUseCase() }
}
