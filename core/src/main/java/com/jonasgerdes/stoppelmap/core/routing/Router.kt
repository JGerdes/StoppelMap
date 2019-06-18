package com.jonasgerdes.stoppelmap.core.routing

object Router {

    enum class BackNavigationResult {
        HANDLED,
        EMPTY_STACK
    }

    var navigator: Navigator? = null

    fun navigateToRoute(route: Route) {
        navigator?.navigateToRoute(route)
    }

    fun navigateBack(): BackNavigationResult {
        return navigator?.navigateBack() ?: BackNavigationResult.EMPTY_STACK
    }


    interface Navigator {
        fun navigateToRoute(route: Route)
        fun navigateBack(): BackNavigationResult
    }
}