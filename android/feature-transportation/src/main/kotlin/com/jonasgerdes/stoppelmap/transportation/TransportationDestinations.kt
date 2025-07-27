package com.jonasgerdes.stoppelmap.transportation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.jonasgerdes.stoppelmap.transportation.ui.overview.TransportationOverviewScreen
import com.jonasgerdes.stoppelmap.transportation.ui.route.RouteScreen
import com.jonasgerdes.stoppelmap.transportation.ui.station.StationScreen
import kotlinx.serialization.Serializable

@Serializable
data object TransportationDestination

@Serializable
data object TransportationOverviewDestination

@Serializable
data class TransportRouteDestination(val routeId: String)

@Serializable
data class TransportStationDestination(val stationId: String)

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
                onRouteTap = {
                    navController.navigate(TransportRouteDestination(routeId = it))
                },
                onStationTap = {
                    navController.navigate(TransportStationDestination(stationId = it))
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
                onStationTap = {
                    navController.navigate(TransportStationDestination(stationId = it))
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
                onNavigateBack = { navController.navigateUp() },
                Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            )
        }
    }
}