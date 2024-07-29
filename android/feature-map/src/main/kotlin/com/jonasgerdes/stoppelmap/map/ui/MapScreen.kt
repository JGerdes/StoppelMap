package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jonasgerdes.stoppelmap.base.model.MapDataFile
import com.jonasgerdes.stoppelmap.map.components.Map
import org.koin.androidx.compose.get
import timber.log.Timber

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    scaffoldPadding: PaddingValues,
    onRequestLocationPermission: () -> Unit,
) {
    val mapDataFile: MapDataFile = get()

    Box(modifier = modifier) {
        Map(
            mapState = MapState(),
            onCameraUpdateDispatched = { },
            onCameraMoved = { },
            onStallTap = { },
            mapDataFile = "file://${mapDataFile.file.absolutePath}".also { Timber.d("mapFile: $it") },
            colors = MapTheme().toMapColors(),
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
        )
    }
}
