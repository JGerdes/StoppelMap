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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
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
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.model.AppUpdateType
import com.jonasgerdes.stoppelmap.home.ui.HomeScreen
import com.jonasgerdes.stoppelmap.map.mapDestinations
import com.jonasgerdes.stoppelmap.map.repository.AndroidPermissionRepository
import com.jonasgerdes.stoppelmap.navigation.AboutDestination
import com.jonasgerdes.stoppelmap.navigation.HomeDestination
import com.jonasgerdes.stoppelmap.navigation.StartDestination
import com.jonasgerdes.stoppelmap.navigation.navigationTabs
import com.jonasgerdes.stoppelmap.news.newsDestinations
import com.jonasgerdes.stoppelmap.news.usecase.GetUnreadNewsCountUseCase
import com.jonasgerdes.stoppelmap.schedule.scheduleDestinations
import com.jonasgerdes.stoppelmap.settings.data.Settings
import com.jonasgerdes.stoppelmap.settings.ui.SettingsScreen
import com.jonasgerdes.stoppelmap.settings.usecase.GetSettingsUseCase
import com.jonasgerdes.stoppelmap.theme.StoppelMapTheme
import com.jonasgerdes.stoppelmap.theme.settings.ColorSchemeSetting
import com.jonasgerdes.stoppelmap.theme.settings.ThemeSetting
import com.jonasgerdes.stoppelmap.transportation.transportationDestinations
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class StoppelMapActivity : ComponentActivity() {

    private val permissionRepository: AndroidPermissionRepository by inject()
    private val appUpdateManager: AppUpdateManager by inject()
    private val getSettings: GetSettingsUseCase by inject()
    private val getUnreadNewsCount: GetUnreadNewsCountUseCase by inject()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            permissionRepository.update()
        }

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

                    navigationTabs.forEach { tab ->
                        NavigationBarItem(
                            icon = tab.iconComposable,
                            label = {
                                Text(
                                    text = stringResource(tab.label),
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            selected = currentDestination?.hierarchy?.any {
                                it.hasRoute(tab.startDestination::class)
                            } == true,
                            onClick = {
                                navController.navigate(tab.startDestination) {
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
                modifier = Modifier
                    .padding(scaffoldPadding)
                    .consumeWindowInsets(scaffoldPadding),
                startDestination = StartDestination,
            ) {
                navigation<StartDestination>(
                    startDestination = HomeDestination
                ) {
                    composable<HomeDestination> {
                        HomeScreen(
                            onUrlTap = { openUrl(it) },
                            onSettingsOptionTap = { navController.navigate(AboutDestination) },
                            onDownloadUpdateTap = { startUpdateDownload(it) },
                            onOpenGooglePlayTap = { openGooglePlayPage() },
                            onCallPhoneNumber = { dialPhoneNumber(it) },
                            modifier = Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        )
                    }
                    composable<AboutDestination> {
                        SettingsScreen(
                            onNavigateBack = { navController.navigateUp() },
                            onUrlTap = { openUrl(it) },
                            Modifier
                                .fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        )
                    }
                }
                mapDestinations(
                    onRequestLocationPermission = ::requestLocationPermission,
                )
                scheduleDestinations()
                transportationDestinations(
                    navController = navController,
                    onDialPhoneNumber = ::dialPhoneNumber,
                    onOpenUrl = ::openUrl,
                )
                newsDestinations(
                    onOpenUrl = ::openUrl,
                )
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
