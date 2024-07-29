package com.jonasgerdes.stoppelmap.map.ui

import com.jonasgerdes.stoppelmap.map.MapDefaults
import com.jonasgerdes.stoppelmap.theme.settings.MapColorSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting
import org.maplibre.android.geometry.LatLng

data class MapState(
    val camera: Camera? = initialCamera,
    val cameraMovementSource: CameraMovementSource = CameraMovementSource.Computed,
    val highlightedEntities: List<Unit>? = null
) {

    enum class CameraMovementSource {
        User, Computed
    }
}

sealed interface Camera {
    data class FocusLocation(val location: LatLng, val zoom: Double) : Camera
    data class IncludeLocations(val locations: List<LatLng>) : Camera
}

private val initialCamera = Camera.FocusLocation(
    MapDefaults.center,
    MapDefaults.defaultZoom
)

data class MapTheme(
    val mapColorSetting: MapColorSetting = MapColorSetting.default,
    val themeSetting: ThemeSetting = ThemeSetting.default,
)