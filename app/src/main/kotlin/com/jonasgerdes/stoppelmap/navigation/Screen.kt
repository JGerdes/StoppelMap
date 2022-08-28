package com.jonasgerdes.stoppelmap.navigation

sealed class Screen(val route: String) {

    object Home : Screen("home")
    object About : Screen("about")
    object Map : Screen("map")
    object Schedule : Screen("schedule")
    object TransportationOverview : Screen("transportation")
    object TransportRoute : Screen("transportation/route/{routeId}") {
        fun create(routeId: String) = "transportation/route/$routeId"
    }

    object TransportStation : Screen("transportation/station/{stationId}") {
        fun create(stationId: String) = "transportation/station/$stationId"
    }

    object News : Screen("news")

}
