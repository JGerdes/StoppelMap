package com.jonasgerdes.stoppelmap.view

import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.core.widget.saveProcessRoute
import java.util.*
import kotlin.collections.ArrayList

private const val CURRENT_DESTINATION = "CURRENT_DESTINATION"
private const val CURRENT_ROUTE = "CURRENT_ROUTE"
private const val STACK_NAMES = "STACK_NAMES"
private const val BACK_STACK_PREFIX = "BACK_STACK_"

class Navigator(
    private @IdRes val hostViewId: Int,
    private val fragmentManager: FragmentManager,
    private val fragmentFactory: (Route) -> BaseFragment<out Route>,
    initialState: Map<Router.Destination, Route>
) : Router.Navigator {

    private var needsFragmentUpdate: Boolean = false
    private val backStack = mutableMapOf<Router.Destination, Stack<Route>>()
    private var currentDestination: Router.Destination? = null
    private var currentRoute: Route? = null

    private val fragmentCache = mutableMapOf<Route, Fragment>()

    init {
        initialState.forEach { (destination, route) ->
            backStack[destination] = Stack<Route>().apply { add(route) }
        }
    }

    override fun navigateToRoute(
        route: Route,
        destination: Router.Destination,
        clearBackStack: Boolean
    ) {
        if (clearBackStack) {
            backStack[destination]?.clear()
        }
        backStack[destination]?.add(route)
        needsFragmentUpdate = true
        switchToDestination(destination)

    }

    private fun Map<Router.Destination, Stack<Route>>.toDebugString() =
        map { (destination, stack) ->
            destination.name + " (${stack.size}): " + stack.joinToString(", ")
        }.joinToString("\n")

    @Suppress("UNCHECKED_CAST")
    override fun switchToDestination(destination: Router.Destination) {
        Log.d("Navigator", "currentBackStack: \n${backStack.toDebugString()}")
        val currentFragment = currentRoute?.let { route ->
            fragmentManager.findFragmentByTag(route::class.java.name) as BaseFragment<Route>
        }

        if (!needsFragmentUpdate && destination == currentDestination && currentFragment != null) {
            currentFragment.onReselected()
            return
        }
        currentDestination = destination
        needsFragmentUpdate = false
        backStack[destination]?.peek()?.let { route ->
            val fragment = getFragmentForRoute(route)
            (fragment as BaseFragment<Route>).saveProcessRoute(route)
            fragmentManager.beginTransaction().apply {
                currentFragment?.let { hide(it) }
                show(fragment)
            }.commitNowAllowingStateLoss()
            currentRoute = route
        }
    }

    private fun getFragmentForRoute(route: Route) =
        fragmentCache[route] ?: fragmentFactory(route).also { newFragment ->
            fragmentCache[route] = newFragment
            fragmentManager.beginTransaction().apply {
                add(hostViewId, newFragment, route::class.java.name)
            }.commitNowAllowingStateLoss()
        }

    override fun navigateBack(): Router.NavigateBackResult =
        when (val destination = currentDestination) {
            null -> Router.NavigateBackResult.UNHANDLED
            else -> {
                needsFragmentUpdate = true
                if (backStack[destination]?.size ?: 0 <= 1) {
                    Router.NavigateBackResult.UNHANDLED
                } else {
                    backStack[destination]?.pop()
                    switchToDestination(destination)
                    Router.NavigateBackResult.HANDLED
                }
            }
        }
}