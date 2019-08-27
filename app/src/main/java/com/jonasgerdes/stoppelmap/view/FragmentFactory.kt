package com.jonasgerdes.stoppelmap.view

import com.jonasgerdes.androidutil.navigation.FragmentFactory
import com.jonasgerdes.androidutil.navigation.NavigationFragment
import com.jonasgerdes.stoppelmap.about.view.AboutFragment
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.events.view.EventsFragment
import com.jonasgerdes.stoppelmap.home.view.HomeFragment
import com.jonasgerdes.stoppelmap.map.view.MapFragment
import com.jonasgerdes.stoppelmap.news.view.NewsFragment
import com.jonasgerdes.stoppelmap.transport.view.TransportFragment

class FragmentFactoryImpl : FragmentFactory<Route> {
    @Suppress("UNCHECKED_CAST")
    override fun <RouteType : Route> invoke(route: RouteType): NavigationFragment<RouteType> {
        return when (route) {
            is Route.Home -> HomeFragment()
            is Route.About -> AboutFragment()

            is Route.Map -> MapFragment()
            is Route.Schedule -> EventsFragment()
            is Route.Transport -> TransportFragment()

            is Route.News -> NewsFragment()

            else -> HomePlaceholderFragment()
        } as NavigationFragment<RouteType>
    }
}