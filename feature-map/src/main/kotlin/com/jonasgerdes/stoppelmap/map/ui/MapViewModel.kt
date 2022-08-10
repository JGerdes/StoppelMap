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

class MapViewModel(
    private val stallRepository: StallRepository
) : ViewModel() {

    private val mapState = MutableStateFlow(MapState.Default)
    private val searchState = MutableStateFlow(SearchState())

    val state: StateFlow<ViewState> =
        combine(
            mapState.debounce(100),
            searchState,
            ::ViewState
        ).stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = ViewState.Default
        )


    fun onCameraMoved(updatedCameraOptions: CameraOptions) {
        mapState.value = mapState.value.copy(
            cameraOptions = updatedCameraOptions,
            cameraMovementSource = MapState.CameraMovementSource.User
        )
    }

    fun onStallTapped(stallSlug: String) {
        viewModelScope.launch {
            stallRepository.getStall(stallSlug)?.let { stall ->
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
