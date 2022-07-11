package com.jonasgerdes.stoppelmap.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.jonasgerdes.stoppelmap.home.ui.HomeScreen
import com.jonasgerdes.stoppelmap.navigation.Screen
import com.jonasgerdes.stoppelmap.navigation.navigationTabs
import com.jonasgerdes.stoppelmap.ui.components.UnderConstructionPlaceholder
import com.jonasgerdes.stoppelmap.ui.theme.StoppelMapTheme

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
                        selected = currentDestination?.hierarchy?.any { it.route == startDestination } == true,
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
                composable(Screen.Home.route) { HomeScreen(Modifier.fillMaxSize()) }
                composable(Screen.Map.route) { UnderConstructionPlaceholder(Modifier.fillMaxSize()) }
                composable(Screen.Schedule.route) { UnderConstructionPlaceholder(Modifier.fillMaxSize()) }
                composable(Screen.Transportation.route) { UnderConstructionPlaceholder(Modifier.fillMaxSize()) }
            }
        }
    }
}
