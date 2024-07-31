package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.map.components.Map
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    scaffoldPadding: PaddingValues,
    onRequestLocationPermission: () -> Unit,
    viewModel: MapViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        val mapDataPath = state.mapDataPath
        Timber.d("mapDataPath: $mapDataPath")
        if (mapDataPath != null) {
            Map(
                mapState = MapState(),
                onCameraUpdateDispatched = { },
                onCameraMoved = { },
                onStallTap = { },
                mapDataFile = "file://$mapDataPath".also { Timber.d("mapFile: $it") },
                colors = MapTheme().toMapColors(),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(scaffoldPadding)
            )
        }
    }
}
