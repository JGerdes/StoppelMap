package com.jonasgerdes.stoppelmap.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.jonasgerdes.stoppelmap.about.ui.AboutScreen
import com.jonasgerdes.stoppelmap.home.ui.HomeScreen
import com.jonasgerdes.stoppelmap.map.ui.MapScreen
import com.jonasgerdes.stoppelmap.navigation.Screen
import com.jonasgerdes.stoppelmap.navigation.navigationTabs
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import com.jonasgerdes.stoppelmap.transportation.ui.overview.TransportationOverviewScreen
import com.jonasgerdes.stoppelmap.transportation.ui.route.RouteScreen
import com.jonasgerdes.stoppelmap.transportation.ui.station.StationScreen
import com.jonasgerdes.stoppelmap.ui.components.UnderConstructionPlaceholder
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
class StoppelMapActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StoppelMapTheme {
                StoppelMapApp()
            }
        }
    }

    @Composable
    fun StoppelMapApp() {

        val navController = rememberNavController()

        Scaffold(bottomBar = {
            NavigationBar {
                val navBackStackEntry = navController.currentBackStackEntryAsState().value
                val currentDestination = navBackStackEntry?.destination

                navigationTabs.forEach { (icon, label, startDestination) ->
                    Timber.d("Adding NavigationBarItem for #$startDestination - current screen is #${currentDestination?.route}")
                    NavigationBarItem(
                        icon = {
                            Icon(imageVector = icon, contentDescription = null)
                        },
                        label = {
                            Text(
                                text = stringResource(label),
                                textAlign = TextAlign.Center,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        selected = currentDestination?.hierarchy?.any {
                            it.route?.startsWith(
                                startDestination
                            ) ?: false
                        } == true,
                        onClick = {
                            navController.navigate(startDestination) {
                                launchSingleTop = true
                                restoreState = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                            }
                        })
                }
            }
        }) { scaffoldPadding ->
            NavHost(
                navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(scaffoldPadding)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onAboutOptionTap = { navController.navigate(Screen.About.route) },
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
                composable(Screen.About.route) {
                    AboutScreen(
                        onNavigateBack = { navController.navigateUp() },
                        onUrlTap = { openUrl(it) },
                        Modifier.fillMaxSize()
                    )
                }
                composable(Screen.Map.route) {
                    MapScreen(
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
                composable(Screen.Schedule.route) { UnderConstructionPlaceholder(Modifier.fillMaxSize()) }
                composable(Screen.TransportationOverview.route) {
                    TransportationOverviewScreen(
                        onRouteTap = {
                            navController.navigate(
                                Screen.TransportRoute.create(routeId = it)
                            )
                        },
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
                composable(Screen.TransportRoute.route) {
                    val routeId = it.arguments?.getString("routeId")!!
                    RouteScreen(
                        routeId = routeId,
                        onStationTap = {
                            navController.navigate(
                                Screen.TransportStation.create(stationId = it)
                            )
                        },
                        onNavigateBack = { navController.navigateUp() },
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
                composable(Screen.TransportStation.route) {
                    val stationId = it.arguments?.getString("stationId")!!
                    StationScreen(
                        stationId = stationId,
                        onNavigateBack = { navController.navigateUp() },
                        Modifier.fillMaxSize()
                    )
                }
            }
        }
    }


    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }
}
