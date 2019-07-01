package com.jonasgerdes.stoppelmap.view

import com.jonasgerdes.androidutil.navigation.FragmentFactory
import com.jonasgerdes.androidutil.navigation.NavigationFragment
import com.jonasgerdes.stoppelmap.about.view.AboutFragment
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.home.view.HomeFragment
import com.jonasgerdes.stoppelmap.news.view.NewsFragment

class FragmentFactoryImpl : FragmentFactory<Route> {
    @Suppress("UNCHECKED_CAST")
    override fun <RouteType : Route> invoke(route: RouteType): NavigationFragment<RouteType> {
        return when (route) {
            is Route.Home -> HomeFragment()
            is Route.About -> AboutFragment()

            is Route.Map -> WorkInProgressPlaceholderFragment()
            is Route.Schedule -> WorkInProgressPlaceholderFragment()
            is Route.Transport -> WorkInProgressPlaceholderFragment()

            is Route.News -> NewsFragment()

            else -> HomePlaceholderFragment()
        } as NavigationFragment<RouteType>
    }
}