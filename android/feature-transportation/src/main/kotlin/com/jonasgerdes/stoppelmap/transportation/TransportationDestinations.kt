package com.jonasgerdes.stoppelmap.transportation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DepartureBoard
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.jonasgerdes.stoppelmap.theme.navigation.NavigationTab
import com.jonasgerdes.stoppelmap.transportation.ui.overview.TransportationOverviewScreen
import com.jonasgerdes.stoppelmap.transportation.ui.route.RouteScreen
import com.jonasgerdes.stoppelmap.transportation.ui.station.StationScreen
import kotlinx.serialization.Serializable

@Serializable
data object TransportationDestination

@Serializable
data object TransportationOverviewDestination

@Serializable
data class TransportRouteDestination(
    val routeId: String,
    val routeName: String?
)

@Serializable
data class TransportStationDestination(
    val stationId: String,
    val stationName: String?,
)

val transportationNavigationTab = NavigationTab(
    icon = Icons.Rounded.DepartureBoard,
    label = R.string.main_bottom_nav_item_transport,
    startDestination = TransportationDestination
)

fun NavGraphBuilder.transportationDestinations(
    navController: NavController,
    onDialPhoneNumber: (String) -> Unit,
    onOpenUrl: (String) -> Unit,
) {
    navigation<TransportationDestination>(
        startDestination = TransportationOverviewDestination
    ) {
        composable<TransportationOverviewDestination> {
            TransportationOverviewScreen(
                onRouteTap = { id, name ->
                    navController.navigate(
                        TransportRouteDestination(
                            routeId = id,
                            routeName = name,
                        )
                    )
                },
                onStationTap = { id, name ->
                    navController.navigate(TransportStationDestination(stationId = id, stationName = name))
                },
                onPhoneNumberTap = onDialPhoneNumber,
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
        composable<TransportRouteDestination> { backStackEntry ->
            val transportRouteDestination: TransportRouteDestination = backStackEntry.toRoute()
            RouteScreen(
                routeId = transportRouteDestination.routeId,
                routeName = transportRouteDestination.routeName,
                onStationTap = { id, name ->
                    navController.navigate(TransportStationDestination(stationId = id, stationName = name))
                },
                onNavigateUp = { navController.navigateUp() },
                onWebsiteTap = onOpenUrl,
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
        composable<TransportStationDestination> { backStackEntry ->
            val transportStationDestination: TransportStationDestination = backStackEntry.toRoute()
            StationScreen(
                stationId = transportStationDestination.stationId,
                stationName = transportStationDestination.stationName,
                onNavigateBack = { navController.navigateUp() },
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}