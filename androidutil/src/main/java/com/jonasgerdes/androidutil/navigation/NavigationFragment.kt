package com.jonasgerdes.androidutil.navigation

import androidx.fragment.app.Fragment

open class NavigationFragment<R> : Fragment() {
    open fun onReselected() {}
    open fun processRoute(route: R) {}
}


interface FragmentFactory<Route> {
    operator fun <Type : Route> invoke(route: Type): NavigationFragment<Type>
}