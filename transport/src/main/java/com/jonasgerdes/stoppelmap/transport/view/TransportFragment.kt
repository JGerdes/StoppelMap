package com.jonasgerdes.stoppelmap.transport.view

import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.transport.R
import com.jonasgerdes.stoppelmap.transport.view.route.RouteDetailFragment
import com.jonasgerdes.stoppelmap.transport.view.station.StationDetailFragment

class TransportFragment : BaseFragment<Route.Transport>(R.layout.fragment_transport) {

    override fun processRoute(route: Route.Transport) {
        val fragment = when (val state = route.state) {
            is Route.Transport.State.OptionsList -> OptionListFragment()
            is Route.Transport.State.RouteDetail -> RouteDetailFragment.newInstance(state.route, state.title)
            is Route.Transport.State.StationDetail -> StationDetailFragment.newInstance(state.station, state.title)
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNowAllowingStateLoss()
    }
}