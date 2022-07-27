package com.jonasgerdes.stoppelmap.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object About : Screen("about")
    object Map : Screen("map")
    object Schedule : Screen("schedule")
    object TransportationOverview : Screen("transportation")

}
