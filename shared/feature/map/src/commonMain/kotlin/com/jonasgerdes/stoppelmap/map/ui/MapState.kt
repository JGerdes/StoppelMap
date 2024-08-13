package com.jonasgerdes.stoppelmap.map.ui

import com.jonasgerdes.stoppelmap.map.model.BoundingBox
import com.jonasgerdes.stoppelmap.map.model.Location
import com.jonasgerdes.stoppelmap.map.model.MapIcon
import com.jonasgerdes.stoppelmap.map.model.SensorLocation

data class MapState(
    val camera: CameraView? = initialCamera,
    val cameraMovementSource: CameraMovementSource = CameraMovementSource.Computed,
    val highlightedEntities: List<HighlightedEntity>? = null,
    val ownLocation: SensorLocation? = null
) {

    enum class CameraMovementSource {
        User, Computed
    }

    data class HighlightedEntity(
        val location: Location,
        val name: String?,
        val icon: MapIcon
    )
}

sealed interface CameraView {
    data class FocusLocation(val location: Location, val zoom: Double? = null) : CameraView
    data class Bounding(val bounds: BoundingBox) : CameraView
}

private val initialCamera = CameraView.FocusLocation(
    MapDefaults.center,
    MapDefaults.defaultZoom
)
