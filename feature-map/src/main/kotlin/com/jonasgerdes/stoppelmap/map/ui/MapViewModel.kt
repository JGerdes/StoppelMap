package com.jonasgerdes.stoppelmap.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.data.Stall
import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.jonasgerdes.stoppelmap.map.model.SearchResult
import com.jonasgerdes.stoppelmap.map.repository.LocationRepository
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.jonasgerdes.stoppelmap.map.usecase.IsLocationInAreaUseCase
import com.jonasgerdes.stoppelmap.map.usecase.SearchStallsUseCase
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapViewModel(
    private val stallRepository: StallRepository,
    private val searchStalls: SearchStallsUseCase,
    private val locationRepository: LocationRepository,
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

    private val userCameraUpdates = MutableStateFlow(initialCameraOptions)

    init {
        viewModelScope.launch {
            userCameraUpdates.debounce(300)
                .collectLatest {
                    mapState.value = mapState.value.copy(
                        cameraPosition = MapState.CameraPosition.Options(it),
                        cameraMovementSource = MapState.CameraMovementSource.User
                    )
                }
        }
    }

    private val effectiveMapState = combine(
        mapState,
        locationState,
    ) { mapState, location ->
        if (location != null) {
            mapState.copy(
                userLocation = Point.fromLngLat(location.longitude, location.latitude)
            )
        } else mapState
    }

    val state: StateFlow<ViewState> =
        combine(
            effectiveMapState,
            searchState,
            snackbarState,
            ::ViewState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState.Default
        )

    private var fetchCurrentLocationJob: Job? = null
    fun onMyLocationFabTap() {
        fetchCurrentLocationJob = viewModelScope.launch {
            locationRepository.getLastKnownLocation()?.let { userLocation ->
                resetSnackbarJob?.cancel()
                if (isLocationInArea(userLocation)) {
                    snackbarState.value = SnackbarState.Hidden
                    mapState.value = mapState.value.copy(
                        cameraPosition = MapState.CameraPosition.Options(
                            CameraOptions.Builder()
                                .center(
                                    Point.fromLngLat(
                                        userLocation.longitude,
                                        userLocation.latitude
                                    )
                                )
                                .zoom(MapDefaults.detailZoom)
                                .build()
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

    fun onCameraMoved(updatedCameraOptions: CameraOptions) {
        fetchCurrentLocationJob?.cancel()
        userCameraUpdates.value = updatedCameraOptions
    }

    fun onStallTapped(stallSlug: String) {
        fetchCurrentLocationJob?.cancel()
        searchState.value = SearchState.Collapsed
        mapState.value = mapState.value.copy(highlightedStalls = null)

        viewModelScope.launch {
            stallRepository.getStall(stallSlug)?.let { stall ->
                mapState.value = mapState.value.copy(
                    cameraPosition = MapState.CameraPosition.Options(
                        CameraOptions.Builder()
                            .center(Point.fromLngLat(stall.center_lng, stall.center_lat))
                            .zoom(MapDefaults.detailZoom)
                            .build()
                    ),
                    cameraMovementSource = MapState.CameraMovementSource.Computed
                )
            }
        }
    }

    fun onSearchButtonTapped() {
        searchState.value = SearchState.Search()
        mapState.value = mapState.value.copy(highlightedStalls = null)
    }

    fun onCloseSearchTapped() {
        searchState.value = SearchState.Collapsed
        mapState.value = mapState.value.copy(highlightedStalls = null)
    }

    fun onSearchResultTapped(result: SearchResult) {
        searchState.value = SearchState.HighlightResult(result)
        mapState.value = mapState.value.copy(
            cameraPosition = MapState.CameraPosition.BoundingCoordinates(result.stalls.map {
                Point.fromLngLat(
                    it.center_lng,
                    it.center_lat
                )
            }),
            cameraMovementSource = MapState.CameraMovementSource.Computed,
            highlightedStalls = result.stalls
        )
    }

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        searchState.value = when (val currentState = searchState.value) {
            SearchState.Collapsed -> SearchState.Search(query = query)
            is SearchState.HighlightResult -> SearchState.Search(query = query)
            is SearchState.Search -> currentState.copy(query = query)
        }
        searchJob?.cancel()

        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            val results = searchStalls(query)
            searchState.value = SearchState.Search(query = query, results = results)
        }

    }

    data class ViewState(
        val mapState: MapState,
        val searchState: SearchState,
        val snackbarState: SnackbarState,
    ) {

        companion object {
            val Default = ViewState(
                mapState = MapState.Default,
                searchState = SearchState.Collapsed,
                snackbarState = SnackbarState.Hidden,
            )
        }
    }

    data class MapState(
        val cameraPosition: CameraPosition,
        val cameraMovementSource: CameraMovementSource,
        val userLocation: Point? = null,
        val highlightedStalls: List<Stall>? = null
    ) {
        companion object {
            val Default = MapState(
                cameraPosition = CameraPosition.Options(initialCameraOptions),
                cameraMovementSource = CameraMovementSource.User
            )
        }

        sealed interface CameraPosition {
            data class Options(val cameraOptions: CameraOptions) : CameraPosition
            data class BoundingCoordinates(val coordinates: List<Point>) : CameraPosition
        }

        enum class CameraMovementSource {
            User, Computed
        }
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

private val initialCameraOptions = CameraOptions.Builder()
    .center(MapDefaults.center)
    .zoom(MapDefaults.defaultZoom)
    .build()
