package com.jonasgerdes.stoppelmap.core.routing

object Router {

    var navigator: Navigator? = null

    fun navigateToRoute(route: Route) {
        navigator?.navigateToRoute(route)
    }


    interface Navigator {
        fun navigateToRoute(route: Route)
    }
}