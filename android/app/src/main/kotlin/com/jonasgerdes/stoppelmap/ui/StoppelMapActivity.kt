package com.jonasgerdes.stoppelmap.ui

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
import com.jonasgerdes.stoppelmap.home.ui.HomeScreen
import com.jonasgerdes.stoppelmap.map.repository.PermissionRepository
import com.jonasgerdes.stoppelmap.navigation.Screen
import com.jonasgerdes.stoppelmap.navigation.navigationTabs
import com.jonasgerdes.stoppelmap.news.ui.NewsScreen
import com.jonasgerdes.stoppelmap.news.usecase.GetUnreadNewsCountUseCase
import com.jonasgerdes.stoppelmap.schedule.ui.ScheduleScreen
import com.jonasgerdes.stoppelmap.settings.data.Settings
import com.jonasgerdes.stoppelmap.settings.ui.SettingsScreen
import com.jonasgerdes.stoppelmap.settings.usecase.GetSettingsUseCase
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import com.jonasgerdes.stoppelmap.theme.settings.ColorSchemeSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting
import com.jonasgerdes.stoppelmap.transportation.ui.route.RouteScreen
import com.jonasgerdes.stoppelmap.transportation.ui.station.StationScreen
import com.jonasgerdes.stoppelmap.ui.components.UnderConstructionPlaceholder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class StoppelMapActivity : ComponentActivity() {

    private val permissionRepository: PermissionRepository by inject()
    private val appUpdateManager: AppUpdateManager by inject()
    private val getSettings: GetSettingsUseCase by inject()
    private val getUnreadNewsCount: GetUnreadNewsCountUseCase by inject()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            permissionRepository.update()
        }

    private var unreadNewsCount: Long by mutableStateOf(0L)

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        var settingsState: Settings? by mutableStateOf(null)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                getSettings()
                    .onEach {
                        settingsState = it
                    }
                    .collect()
            }
        }

        getUnreadNewsCount()
            .onEach {
                unreadNewsCount = it
            }
            .launchIn(lifecycleScope)

        splashScreen.setKeepOnScreenCondition {
            settingsState == null
        }

        setContent {
            StoppelMapTheme(
                themeSetting = settingsState?.themeSetting ?: ThemeSetting.Light,
                colorSchemeSetting = settingsState?.colorSchemeSetting
                    ?: ColorSchemeSetting.Classic
            ) {
                StoppelMapApp()
            }
        }
    }

    @Composable
    fun StoppelMapApp() {
        val navController = rememberNavController()
        Scaffold(
            contentWindowInsets = WindowInsets.navigationBars,
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry = navController.currentBackStackEntryAsState().value
                    val currentDestination = navBackStackEntry?.destination

                    navigationTabs.forEach { (icon, label, startDestination) ->
                        NavigationBarItem(
                            icon = {
                                if (icon == Icons.Rounded.Newspaper) {
                                    BadgedBox(badge = {
                                        androidx.compose.animation.AnimatedVisibility(
                                            visible = unreadNewsCount > 0,
                                            enter = fadeIn() + scaleIn(),
                                            exit = fadeOut() + scaleOut(),
                                        ) {
                                            Badge {
                                                Text(text = unreadNewsCount.toString())
                                            }
                                        }
                                    }) {
                                        Icon(imageVector = icon, contentDescription = null)
                                    }
                                } else Icon(imageVector = icon, contentDescription = null)
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
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        onUrlTap = { openUrl(it) },
                        onSettingsOptionTap = { navController.navigate(Screen.About.route) },
                        onDownloadUpdateTap = { startUpdateDownload(it) },
                        onOpenGooglePlayTap = { openGooglePlayPage() },
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(scaffoldPadding)
                    )
                }
                composable(Screen.About.route) {
                    SettingsScreen(
                        onNavigateBack = { navController.navigateUp() },
                        onUrlTap = { openUrl(it) },
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(scaffoldPadding)
                    )
                }
                composable(Screen.Map.route) {
                    UnderConstructionPlaceholder(Modifier.fillMaxSize())
                }
                composable(Screen.Schedule.route) {
                    ScheduleScreen(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(scaffoldPadding)
                    )
                }
                composable(Screen.TransportationOverview.route) {
                    UnderConstructionPlaceholder(Modifier.fillMaxSize())
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
                            .padding(scaffoldPadding)
                    )
                }
                composable(Screen.TransportStation.route) {
                    val stationId = it.arguments?.getString("stationId")!!
                    StationScreen(
                        stationId = stationId,
                        onNavigateBack = { navController.navigateUp() },
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(scaffoldPadding)
                    )
                }
                composable(Screen.News.route) {
                    NewsScreen(
                        onUrlTap = ::openUrl,
                        Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(scaffoldPadding)
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

    private fun dialPhoneNumber(phoneNumber: String) {
        startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
    }

    private fun requestLocationPermission() {
        if (permissionRepository.isLocationPermissionGranted()) return
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}
