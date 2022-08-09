package com.jonasgerdes.stoppelmap.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.jonasgerdes.stoppelmap.map.repository.StallRepository
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

class MapViewModel(
    private val stallRepository: StallRepository
) : ViewModel() {

    private val mapState = MutableStateFlow(MapState.Default)

    val state: StateFlow<ViewState> =
        mapState.debounce(100)
            .map(::ViewState)
            .stateIn(
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

    data class ViewState(
        val mapState: MapState,
    ) {

        companion object {
            val Default = ViewState(
                mapState = MapState.Default,
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
}
