package com.jonasgerdes.stoppelmap.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.mapbox.maps.CameraOptions
import kotlinx.coroutines.flow.*

class MapViewModel() : ViewModel() {

    private val mapState = MutableStateFlow(MapState.Default)

    val state: StateFlow<ViewState> =
        mapState.debounce(300)
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
