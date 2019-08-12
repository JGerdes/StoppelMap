package com.jonasgerdes.stoppelmap.transport.view

import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.transport.R

class TransportFragment : BaseFragment<Route.Transport>(R.layout.fragment_transport) {

    override fun processRoute(route: Route.Transport) {
        val fragment = when (route.state) {
            is Route.Transport.State.OptionsList -> OptionListFragment()
            is Route.Transport.State.RouteDetail -> TODO()
            is Route.Transport.State.StationDetail -> TODO()
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNowAllowingStateLoss()
    }
}