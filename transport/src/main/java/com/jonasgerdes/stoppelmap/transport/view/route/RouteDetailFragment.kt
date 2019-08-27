package com.jonasgerdes.stoppelmap.transport.view.route

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.transport.R
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_option_list.toolbar
import kotlinx.android.synthetic.main.fragment_route_detail.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class RouteDetailFragment : Fragment(R.layout.fragment_route_detail) {

    val stationAdapter = GroupAdapter<ViewHolder>()

    val title: String? by lazy { arguments!!.getString(ARGUMENT_TITLE) }
    val slug: String by lazy { arguments!!.getString(ARGUMENT_SLUG) }

    val viewModel: RouteDetailViewModel by viewModel { parametersOf(slug) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()
        if (title != null) toolbar.title = title
        toolbar.setNavigationOnClickListener {
            Router.navigateBack()
        }

        stationList.adapter = stationAdapter
        stationAdapter.setOnItemClickListener { item, view ->
            when (item) {
                is StationItem -> Router.navigateToRoute(
                    Route.Transport(
                        state = Route.Transport.State.StationDetail(
                            station = item.stationInformation.basicInfo.slug,
                            title = item.stationInformation.basicInfo.name
                        )
                    ),
                    destination = Router.Destination.TRANSPORT
                )
            }
        }

        observe(viewModel.route) { route ->
            toolbar.title = route.basicInfo.name
            stationAdapter.update(route.stations.map {
                StationItem(it)
            } + listOf(DestinationStationItem {
                Router.navigateToRoute(
                    Route.Transport(
                        state = Route.Transport.State.StationDetail(
                            station = route.returnStation.slug,
                            title = getString(R.string.transport_return_route),
                            subtitle = route.basicInfo.name
                        )
                    ),
                    destination = Router.Destination.TRANSPORT
                )
            }))
        }
    }

    companion object {
        private const val ARGUMENT_SLUG = "argument_slug"
        private const val ARGUMENT_TITLE = "argument_title"
        fun newInstance(slug: String, title: String? = null) = RouteDetailFragment().apply {
            arguments = bundleOf(
                ARGUMENT_SLUG to slug,
                ARGUMENT_TITLE to title
            )
        }
    }
}