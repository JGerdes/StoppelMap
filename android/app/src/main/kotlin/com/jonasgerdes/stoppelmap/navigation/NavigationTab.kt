package com.jonasgerdes.stoppelmap.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DepartureBoard
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Map
import androidx.compose.ui.graphics.vector.ImageVector
import com.jonasgerdes.stoppelmap.R

data class NavigationTab(
    val icon: ImageVector, @StringRes val label: Int, val startRoute: String
)

val navigationTabs = listOf(
    NavigationTab(
        icon = Icons.Rounded.Home,
        label = R.string.main_bottom_nav_item_home,
        startRoute = Screen.Home.route
    ),
    NavigationTab(
        icon = Icons.Rounded.Map,
        label = R.string.main_bottom_nav_item_map,
        startRoute = Screen.Map.route
    ),
    NavigationTab(
        icon = Icons.Rounded.Event,
        label = R.string.main_bottom_nav_item_schedule,
        startRoute = Screen.Schedule.route
    ),
    NavigationTab(
        icon = Icons.Rounded.DepartureBoard,
        label = R.string.main_bottom_nav_item_transport,
        startRoute = Screen.TransportationOverview.route
    ),
)
