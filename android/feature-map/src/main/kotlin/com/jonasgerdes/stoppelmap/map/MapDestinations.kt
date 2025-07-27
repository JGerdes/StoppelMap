package com.jonasgerdes.stoppelmap.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jonasgerdes.stoppelmap.map.ui.MapScreen
import kotlinx.serialization.Serializable

@Serializable
data object MapDestination

fun NavGraphBuilder.mapDestinations(onRequestLocationPermission: () -> Unit) {
    composable<MapDestination> {
        MapScreen(
            onRequestLocationPermission = onRequestLocationPermission,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}