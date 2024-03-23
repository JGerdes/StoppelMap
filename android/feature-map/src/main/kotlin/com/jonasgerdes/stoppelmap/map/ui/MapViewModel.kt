@file:OptIn(FlowPreview::class)

package com.jonasgerdes.stoppelmap.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.data.Stall
import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.repository.SearchHistoryRepository
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.repository.location.LocationRepository
import com.jonasgerdes.stoppelmap.map.usecase.GetSearchHistoryUseCase
import com.jonasgerdes.stoppelmap.map.usecase.IsLocationInAreaUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchStallsUseCase
import com.jonasgerdes.stoppelmap.settings.data.SettingsRepository
import com.jonasgerdes.stoppelmap.theme.settings.MapColorSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.maplibre.android.geometry.LatLng

class MapViewModel(
    private val stallRepository: StallRepository,
    private val searchStalls: SearchStallsUseCase,
    private val getSearchHistory: GetSearchHistoryUseCase,
    private val searchHistoryRepository: SearchHistoryRepository,
    private val locationRepository: LocationRepository,
    private val settingsRepository: SettingsRepository,
    private val isLocationInArea: IsLocationInAreaUseCase,
) : ViewModel() {

    private val mapState = MutableStateFlow(MapState.Default)
    private val searchState = MutableStateFlow<SearchState>(SearchState.Collapsed)
    private val snackbarState = MutableStateFlow<SnackbarState>(SnackbarState.Hidden)

    private var resetSnackbarJob: Job? = null

    private val locationState = locationRepository.getLocationUpdates()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null
        )

    private var searchHistory: List<SearchResult> = emptyList()

    init {
        getSearchHistory()
            .onEach {
                searchHistory = it
            }
            .launchIn(viewModelScope)
    }


    private val mapThemeState = settingsRepository.getSettings()
        .map {
            MapTheme(
                mapColorSetting = it.mapColorSetting,
                themeSetting = it.themeSetting,
            )
        }

    val state: StateFlow<ViewState> =
        combine(
            mapState,
            mapThemeState,
            searchState,
            snackbarState,
            ::ViewState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState()
        )

    private var fetchCurrentLocationJob: Job? = null
    fun onMyLocationFabTap() {
        fetchCurrentLocationJob = viewModelScope.launch {
            locationRepository.getLastKnownLocation()?.let { userLocation ->
                resetSnackbarJob?.cancel()
                if (isLocationInArea(userLocation)) {
                    snackbarState.value = SnackbarState.Hidden
                    mapState.value = mapState.value.copy(
                        camera = Camera.FocusLocation(
                            LatLng(
                                latitude = userLocation.latitude,
                                longitude = userLocation.longitude
                            ),
                            zoom = MapDefaults.detailZoom
                        ),
                        cameraMovementSource = MapState.CameraMovementSource.Computed
                    )
                } else {
                    snackbarState.value =
                        SnackbarState.Shown(text = "Du befindest dich nicht auf dem Festgelände!")

                    resetSnackbarJob = viewModelScope.launch {
                        delay(5000)
                        snackbarState.value = SnackbarState.Hidden
                    }
                }
            }
        }
    }

    fun onCameraUpdateDispatched() {
        mapState.update {
            it.copy(camera = null)
        }
    }

    fun onCameraMoved() {
        fetchCurrentLocationJob?.cancel()
    }

    fun onStallTapped(stallSlug: String) {
        fetchCurrentLocationJob?.cancel()
        searchState.value = SearchState.Collapsed
        mapState.value = mapState.value.copy(highlightedStalls = null)

        viewModelScope.launch {
            stallRepository.getStall(stallSlug)?.let { stall ->
                mapState.value = mapState.value.copy(
                    camera = Camera.FocusLocation(
                        LatLng(latitude = stall.center_lat, longitude = stall.center_lng),
                        MapDefaults.detailZoom
                    ),
                    cameraMovementSource = MapState.CameraMovementSource.Computed
                )
            }
        }
    }

    fun onSearchButtonTapped() {
        searchState.value = SearchState.Search(results = searchHistory)
        mapState.value = mapState.value.copy(highlightedStalls = null)
    }

    fun onCloseSearchTapped() {
        searchState.value = SearchState.Collapsed
        mapState.value = mapState.value.copy(highlightedStalls = null)
    }

    fun onSearchResultTapped(result: SearchResult) {
        searchState.value = SearchState.HighlightResult(result)
        mapState.value = mapState.value.copy(
            camera = Camera.IncludeLocations(
                result.stalls.map {
                    LatLng(latitude = it.center_lat, longitude = it.center_lng)
                }
            ),
            cameraMovementSource = MapState.CameraMovementSource.Computed,
            highlightedStalls = result.stalls
        )
        viewModelScope.launch {
            searchHistoryRepository.saveResultToHistory(result)
        }
    }

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        searchState.value = when (val currentState = searchState.value) {
            SearchState.Collapsed -> SearchState.Search(query = query)
            is SearchState.HighlightResult -> SearchState.Search(query = query)
            is SearchState.Search -> currentState.copy(query = query)
        }
        searchJob?.cancel()

        if (query.isBlank()) {
            searchState.value = SearchState.Search(query = query, results = searchHistory)
        } else {
            searchJob = viewModelScope.launch {
                val results = searchStalls(query)
                searchState.value = SearchState.Search(query = query, results = results)
            }
        }
    }

    data class ViewState(
        val mapState: MapState = MapState.Default,
        val mapTheme: MapTheme = MapTheme(),
        val searchState: SearchState = SearchState.Collapsed,
        val snackbarState: SnackbarState = SnackbarState.Hidden,
    )

    data class MapState(
        val camera: Camera?,
        val cameraMovementSource: CameraMovementSource,
        val highlightedStalls: List<Stall>? = null
    ) {

        companion object {
            val Default = MapState(
                camera = initialCamera,
                cameraMovementSource = CameraMovementSource.Computed
            )
        }

        enum class CameraMovementSource {
            User, Computed
        }
    }

    sealed interface Camera {
        data class FocusLocation(val location: LatLng, val zoom: Double) : Camera
        data class IncludeLocations(val locations: List<LatLng>) : Camera
    }

    sealed interface SnackbarState {
        object Hidden : SnackbarState
        data class Shown(val text: String) : SnackbarState
    }

    sealed interface SearchState {
        object Collapsed : SearchState
        data class Search(
            val query: String = "",
            val results: List<SearchResult> = emptyList()
        ) : SearchState

        data class HighlightResult(val result: SearchResult) : SearchState
    }
}

data class MapTheme(
    val mapColorSetting: MapColorSetting = MapColorSetting.default,
    val themeSetting: ThemeSetting = ThemeSetting.default,
)

private val initialCamera = MapViewModel.Camera.FocusLocation(
    MapDefaults.center,
    MapDefaults.defaultZoom
)
