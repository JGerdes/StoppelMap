package com.jonasgerdes.stoppelmap.map


import android.content.Context
import android.location.Location
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.gms.location.LocationServices
import com.jonasgerdes.stoppelmap.data.StoppelMapDatabase
import com.jonasgerdes.stoppelmap.map.repository.AliasRepository
import com.jonasgerdes.stoppelmap.map.repository.ItemRepository
import com.jonasgerdes.stoppelmap.map.repository.PermissionRepository
import com.jonasgerdes.stoppelmap.map.repository.SearchHistoryRepository
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.repository.TypeRepository
import com.jonasgerdes.stoppelmap.map.repository.location.FakeLocationRepository
import com.jonasgerdes.stoppelmap.map.repository.location.LocationRepository
import com.jonasgerdes.stoppelmap.map.repository.location.LocationRepositoryImpl
import com.jonasgerdes.stoppelmap.map.repository.location.LocationRepositoryWrapper
import com.jonasgerdes.stoppelmap.map.repository.location.MapBoxLocationProvider
import com.jonasgerdes.stoppelmap.map.ui.MapViewModel
import com.jonasgerdes.stoppelmap.map.usecase.GetSearchHistoryUseCase
import com.jonasgerdes.stoppelmap.map.usecase.InitializeMapBoxUseCase
import com.jonasgerdes.stoppelmap.map.usecase.IsLocationInAreaUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchStallsUseCase
import com.jonasgerdes.stoppelmap.settings.data.LocationOverride
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "mapUserData")

val mapModule = module {

    single { StallRepository(stallQueries = get<StoppelMapDatabase>().stallQueries) }
    single { TypeRepository(typeQueries = get<StoppelMapDatabase>().sub_typesQueries) }
    single { ItemRepository(itemQueries = get<StoppelMapDatabase>().itemQueries) }
    single { AliasRepository(aliasQueries = get<StoppelMapDatabase>().aliasQueries) }

    single { PermissionRepository(context = get()) }

    single { LocationServices.getFusedLocationProviderClient(get<Context>()) }
    single<LocationRepository> {
        LocationRepositoryWrapper(
            locationRepoMap = mapOf(
                LocationOverride.None to LocationRepositoryImpl(
                    fusedLocationProviderClient = get(),
                    permissionRepository = get()
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

    single { MapBoxLocationProvider(locationRepository = get()) }

    single { InitializeMapBoxUseCase() }

    factory {
        SearchStallsUseCase(
            stallRepository = get(),
            typeRepository = get(),
            itemRepository = get(),
            aliasRepository = get(),
        )
    }
    factory { IsLocationInAreaUseCase() }

    single {
        SearchHistoryRepository(dataStore = get<Context>().dataStore)
    }

    factory {
        GetSearchHistoryUseCase(
            searchHistoryRepository = get(),
            stallRepository = get()
        )
    }

    viewModel {
        MapViewModel(
            stallRepository = get(),
            searchStalls = get(),
            getSearchHistory = get(),
            searchHistoryRepository = get(),
            locationRepository = get(),
            settingsRepository = get(),
            isLocationInArea = get()
        )
    }
}
