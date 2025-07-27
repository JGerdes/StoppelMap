package com.jonasgerdes.stoppelmap.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.DepartureBoard
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Map
import androidx.compose.material.icons.rounded.Newspaper
import androidx.compose.ui.graphics.vector.ImageVector
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.map.MapDestination
import com.jonasgerdes.stoppelmap.news.NewsDestination
import com.jonasgerdes.stoppelmap.schedule.ScheduleDestination
import com.jonasgerdes.stoppelmap.transportation.TransportationDestination

data class NavigationTab(
    val icon: ImageVector, @StringRes val label: Int, val startDestination: Any
)

val navigationTabs = listOf(
    NavigationTab(
        icon = Icons.Rounded.Home,
        label = R.string.main_bottom_nav_item_home,
        startDestination = StartDestination,
    ),
    NavigationTab(
        icon = Icons.Rounded.Map,
        label = R.string.main_bottom_nav_item_map,
        startDestination = MapDestination
    ),
    NavigationTab(
        icon = Icons.Rounded.Event,
        label = R.string.main_bottom_nav_item_schedule,
        startDestination = ScheduleDestination
    ),
    NavigationTab(
        icon = Icons.Rounded.DepartureBoard,
        label = R.string.main_bottom_nav_item_transport,
        startDestination = TransportationDestination
    ),
    NavigationTab(
        icon = Icons.Rounded.Newspaper,
        label = R.string.main_bottom_nav_item_news,
        startDestination = NewsDestination
    ),
)
