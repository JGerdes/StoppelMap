package com.jonasgerdes.stoppelmap.core.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.jonasgerdes.androidutil.navigation.NavigationFragment
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router

abstract class BaseFragment<R: Route>(@LayoutRes val layoutRes: Int) : NavigationFragment<R>() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(layoutRes, container, false)

    override fun onReselected() {
        Router.navigateBack()
    }

    override fun processRoute(route: R) {}
}

@Suppress("UNCHECKED_CAST")
fun <R: Route> BaseFragment<R>.saveProcessRoute(route: Route) {
    try {
        processRoute(route as R)
    } catch (e: ClassCastException) {
        //should technically not happen
        //TODO: maybe log this
    }
}