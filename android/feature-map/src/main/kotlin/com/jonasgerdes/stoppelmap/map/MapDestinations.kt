package com.jonasgerdes.stoppelmap.map

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.jonasgerdes.stoppelmap.map.ui.MapScreen
import com.jonasgerdes.stoppelmap.theme.navigation.NavigationTab
import kotlinx.serialization.Serializable

@Serializable
data object MapDestination

val mapNavigationTab = NavigationTab(
    icon = Icons.Rounded.Map,
    label = R.string.main_bottom_nav_item_map,
    startDestination = MapDestination
)

fun NavGraphBuilder.mapDestinations(
    onRequestLocationPermission: () -> Unit,
    onShareText: (String) -> Unit,
    onOpenUrl: (String) -> Unit,
) {
    composable<MapDestination> {
        MapScreen(
            onRequestLocationPermission = onRequestLocationPermission,
            onShareText = onShareText,
            onOpenUrl = onOpenUrl,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        )
    }
}