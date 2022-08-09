@file:OptIn(ExperimentalLifecycleComposeApi::class)

package com.jonasgerdes.stoppelmap.map.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.jonasgerdes.stoppelmap.map.components.MapboxMap
import org.koin.androidx.compose.viewModel

@Composable
fun MapScreen(
    modifier: Modifier = Modifier,
    lazyViewModel: Lazy<MapViewModel> = viewModel(),
) {
    val viewModel by lazyViewModel
    val state by viewModel.state.collectAsStateWithLifecycle()

    Box(modifier = modifier) {
        MapboxMap(
            mapState = state.mapState,
            onCameraMoved = viewModel::onCameraMoved,
            modifier = Modifier.fillMaxSize()
        )
    }
}
