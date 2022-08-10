package com.jonasgerdes.stoppelmap.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.data.Stall
import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class MapViewModel(
    private val stallRepository: StallRepository
) : ViewModel() {

    private val mapState = MutableStateFlow(MapState.Default)
    private val searchState = MutableStateFlow(SearchState())


    private val userCameraUpdates = MutableStateFlow(MapState.Default.cameraOptions)

    init {
        viewModelScope.launch {
            userCameraUpdates.debounce(300)
                .collectLatest {
                    mapState.value = mapState.value.copy(
                        cameraOptions = it,
                        cameraMovementSource = MapState.CameraMovementSource.User
                    )
                }
        }
    }

    val state: StateFlow<ViewState> =
        combine(
            mapState,
            searchState,
            ::ViewState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState.Default
        )


    fun onCameraMoved(updatedCameraOptions: CameraOptions) {
        userCameraUpdates.value = updatedCameraOptions
    }

    fun onStallTapped(stallSlug: String) {
        Timber.d("onStallTapped: $stallSlug")
        viewModelScope.launch {
            stallRepository.getStall(stallSlug)?.let { stall ->
                Timber.d("found stall: $stall")
                mapState.value = mapState.value.copy(
                    cameraOptions = CameraOptions.Builder()
                        .center(Point.fromLngLat(stall.center_lng, stall.center_lat))
                        .zoom(MapDefaults.detailZoom)
                        .build(),
                    cameraMovementSource = MapState.CameraMovementSource.Computed
                )
            }
        }
    }

    fun onSearchResultTapped(resultSlug: String) {
        searchState.value = searchState.value.copy(query = "")
        onStallTapped(resultSlug)
    }

    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        searchState.value = searchState.value.let {
            it.copy(
                query = query,
                results = if (query.isBlank()) emptyList() else it.results
            )
        }
        searchJob?.cancel()

        if (query.isBlank()) return

        searchJob = viewModelScope.launch {
            val results = stallRepository.findByQuery(query)
            searchState.value =
                searchState.value.copy(results = results.filter { it.name.isNullOrBlank().not() })
        }

    }

    data class ViewState(
        val mapState: MapState,
        val searchState: SearchState,
    ) {

        companion object {
            val Default = ViewState(
                mapState = MapState.Default,
                searchState = SearchState()
            )
        }
    }

    data class MapState(
        val cameraOptions: CameraOptions,
        val cameraMovementSource: CameraMovementSource
    ) {
        companion object {
            val Default = MapState(
                cameraOptions = CameraOptions.Builder()
                    .center(MapDefaults.center)
                    .zoom(MapDefaults.defaultZoom)
                    .build(),
                cameraMovementSource = CameraMovementSource.User
            )
        }

        enum class CameraMovementSource {
            User, Computed
        }
    }

    data class SearchState(
        val query: String = "",
        val results: List<Stall> = emptyList()
    )
}
