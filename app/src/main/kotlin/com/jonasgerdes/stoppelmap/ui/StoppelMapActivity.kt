package com.jonasgerdes.stoppelmap.ui

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
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
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.jonasgerdes.stoppelmap.about.ui.AboutScreen
import com.jonasgerdes.stoppelmap.home.ui.HomeScreen
import com.jonasgerdes.stoppelmap.map.repository.PermissionRepository
import com.jonasgerdes.stoppelmap.map.ui.MapScreen
import com.jonasgerdes.stoppelmap.navigation.Screen
import com.jonasgerdes.stoppelmap.navigation.navigationTabs
import com.jonasgerdes.stoppelmap.news.ui.NewsScreen
import com.jonasgerdes.stoppelmap.schedule.ui.ScheduleScreen
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import com.jonasgerdes.stoppelmap.transportation.ui.overview.TransportationOverviewScreen
import com.jonasgerdes.stoppelmap.transportation.ui.route.RouteScreen
import com.jonasgerdes.stoppelmap.transportation.ui.station.StationScreen
import org.koin.android.ext.android.inject


@OptIn(ExperimentalMaterial3Api::class)
class StoppelMapActivity : ComponentActivity() {

    private val permissionRepository: PermissionRepository by inject()
    private val appUpdateManager: AppUpdateManager by inject()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            permissionRepository.update()
        }

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
                        onUrlTap = { openUrl(it) },
                        onAboutOptionTap = { navController.navigate(Screen.About.route) },
                        onDownloadUpdateTap = { startUpdateDownload(it) },
                        onOpenGooglePlayTap = { openGooglePlayPage() },
                        modifier = Modifier
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
                        onRequestLocationPermission = ::requestLocationPermission,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
                composable(Screen.Schedule.route) {
                    ScheduleScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                    )
                }
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
                composable(Screen.News.route) {
                    NewsScreen(
                        onUrlTap = ::openUrl,
                        Modifier.fillMaxSize()
                    )
                }
            }
        }
    }

    private fun startUpdateDownload(appUpdateInfo: AppUpdateInfo) {
        appUpdateManager.startUpdateFlow(
            appUpdateInfo,
            this,
            AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE).build()
        )
    }

    private fun openGooglePlayPage() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }


    private fun openUrl(url: String) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
    }

    private fun requestLocationPermission() {
        if (permissionRepository.isLocationPermissionGranted()) return
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
