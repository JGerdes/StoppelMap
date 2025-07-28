package com.jonasgerdes.stoppelmap.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.map.mapNavigationTab
import com.jonasgerdes.stoppelmap.news.newsNavigationTab
import com.jonasgerdes.stoppelmap.schedule.scheduleNavigationTab
import com.jonasgerdes.stoppelmap.theme.navigation.NavigationTab
import com.jonasgerdes.stoppelmap.transportation.transportationNavigationTab


val navigationTabs = listOf(
    NavigationTab(
        icon = Icons.Rounded.Home,
        label = R.string.main_bottom_nav_item_home,
        startDestination = StartDestination,
    ),
    mapNavigationTab,
    scheduleNavigationTab,
    transportationNavigationTab,
    newsNavigationTab,
)
