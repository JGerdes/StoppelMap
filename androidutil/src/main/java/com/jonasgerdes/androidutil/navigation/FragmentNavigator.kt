package com.jonasgerdes.androidutil.navigation


import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.FragmentManager
import java.util.*
import kotlin.collections.ArrayList

private const val CURRENT_DESTINATION = "CURRENT_DESTINATION"
private const val CURRENT_ROUTE = "CURRENT_ROUTE"
private const val STACK_NAMES = "STACK_NAMES"
private const val BACK_STACK_PREFIX = "BACK_STACK_"


private fun <T> Stack<T>?.emptyAfterNextPop() = (this?.size ?: 0) <= 1

@Suppress("UNCHECKED_CAST")
private fun <Type : Route, Route> NavigationFragment<Type>.saveProcessRoute(route: Route) {
    try {
        processRoute(route as Type)
    } catch (e: ClassCastException) {
        //should technically not happen
        //TODO: maybe log this
    }
}

inline fun <Route : Parcelable, reified Destination : Enum<Destination>> createFragmentNavigator(
    @IdRes hostViewId: Int,
    fragmentManager: FragmentManager,
    fragmentFactory: FragmentFactory<Route>,
    initialState: Map<Destination, Route>
) = Navigator(
    hostViewId,
    fragmentManager,
    fragmentFactory,
    initialState,
    destinationConverter = { key -> enumValueOf<Destination>(key) }
)


class Navigator<Route : Parcelable, Destination : Enum<Destination>>(
    @IdRes private val hostViewId: Int,
    private val fragmentManager: FragmentManager,
    private val fragmentFactory: FragmentFactory<Route>,
    initialState: Map<Destination, Route>,
    private val destinationConverter: (String) -> Destination
) {

    enum class NavigateBackResult {
        HANDLED,
        UNHANDLED
    }

    private var needsFragmentUpdate: Boolean = false
    private val backStack = mutableMapOf<Destination, Stack<Route>>()
    private var currentDestination: Destination? = null
    private var currentRoute: Route? = null

    private val fragmentCache = mutableMapOf<String, NavigationFragment<out Route>>()

    init {
        initialState.forEach { (destination, route) ->
            backStack[destination] = Stack<Route>().apply { add(route) }
        }
    }

    fun navigateToRoute(route: Route, destination: Destination, clearBackStack: Boolean) {
        if (clearBackStack) {
            backStack[destination]?.clear()
        }
        backStack[destination]?.add(route)
        needsFragmentUpdate = true
        switchToDestination(destination)

    }


    @Suppress("UNCHECKED_CAST")
    fun switchToDestination(destination: Destination) {
        Log.d("Navigator", "currentBackStack: \n${backStack.toDebugString()}")
        val currentFragment = currentRoute?.let { route ->
            fragmentManager.findFragmentByTag(route::class.java.name) as? NavigationFragment<out Route>
        }

        if (!needsFragmentUpdate && destination == currentDestination && currentFragment != null) {
            currentFragment.onReselected()
            return
        }
        currentDestination = destination
        needsFragmentUpdate = false
        backStack[destination]?.peek()?.let { route ->
            val fragment = getFragmentForRoute(route)
            fragment.saveProcessRoute(route)
            if (currentFragment != fragment) {
                fragmentManager.beginTransaction().apply {
                    currentFragment?.let { hide(it) }
                    show(fragment)
                }.commitNowAllowingStateLoss()
            }
            currentRoute = route
        }
    }

    fun onSaveInstanceState(outState: Bundle) {
        outState.putString(CURRENT_DESTINATION, currentDestination?.name)
        outState.putParcelable(CURRENT_ROUTE, currentRoute)

        val list = ArrayList<String>()
        list.addAll(backStack.keys.map { it.name })
        outState.putStringArrayList(STACK_NAMES, list)

        backStack.forEach { (key, stack) ->
            val stackList = ArrayList<Parcelable>()
            stackList.addAll(stack)
            outState.putParcelableArrayList("$BACK_STACK_PREFIX$key", stackList)
        }
    }

    fun onLoadState(state: Bundle) {
        val stackNames = state.getStringArrayList(STACK_NAMES)

        stackNames.forEach { key ->
            val destination = destinationConverter(key)
            val stack: ArrayList<Route> = state.getParcelableArrayList("$BACK_STACK_PREFIX$key")
            backStack[destination] = Stack<Route>().apply {
                addAll(stack)
            }
        }
        currentDestination = state.getString(CURRENT_DESTINATION)?.let {
            destinationConverter(it)
        }
        currentRoute = state.getParcelable(CURRENT_ROUTE)
    }

    fun navigateBack(): NavigateBackResult = when (val destination = currentDestination) {
        null -> NavigateBackResult.UNHANDLED
        else -> {
            needsFragmentUpdate = true
            if (backStack[destination].emptyAfterNextPop()) {
                NavigateBackResult.UNHANDLED
            } else {
                backStack[destination]?.pop()
                switchToDestination(destination)
                NavigateBackResult.HANDLED
            }
        }
    }

    private fun Map<Destination, Stack<Route>>.toDebugString() = map { (destination, stack) ->
        destination.name + " (${stack.size}): " + stack.joinToString(", ")
    }.joinToString("\n")

    private fun getFragmentForRoute(route: Route): NavigationFragment<out Route> =
        fragmentCache[route::class.java.name] ?: fragmentFactory(route).also { newFragment ->
            fragmentCache[route::class.java.name] = newFragment
            fragmentManager.beginTransaction().apply {
                add(hostViewId, newFragment, route::class.java.name)
            }.commitNowAllowingStateLoss()
        }
}
