package com.jonasgerdes.stoppelmap.core.routing

object Router {

    var navigator: Navigator? = null

    fun navigateToRoute(route: Route, destination: Destination, clearBackStack:Boolean = false) {
        navigator?.navigateToRoute(route, destination, clearBackStack)
    }

    fun navigateBack(): NavigateBackResult {
        return navigator?.navigateBack() ?: Router.NavigateBackResult.UNHANDLED
    }

    fun switchToDestination(destination: Destination) {
        navigator?.switchToDestination(destination)
    }


    interface Navigator {
        fun navigateToRoute(route: Route, destination: Destination, clearBackStack: Boolean = false)
        fun switchToDestination(destination: Destination)
        fun navigateBack(): NavigateBackResult
    }

    enum class NavigateBackResult {
        HANDLED,
        UNHANDLED
    }

    enum class Destination {
        HOME,
        MAP,
        SCHEDULE,
        TRANSPORT,
        NEWS
    }
}