package com.jonasgerdes.stoppelmap.transport.view

import android.os.Bundle
import android.view.View
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.transport.R
import com.xwray.groupie.Group
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_transport.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TransportFragment : BaseFragment<Route.Schedule>(R.layout.fragment_transport) {

    val transportAdapter = GroupAdapter<ViewHolder>()

    val viewModel: TransportViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()

        transportList.adapter = transportAdapter

        observe(viewModel.transportOptions) { options ->
            val sections = mutableListOf<Group>()
            if (options.bus != null) {
                sections.add(Section(
                    CategoryTitle(getString(R.string.transport_category_title_bus)),
                    options.bus.map { TransportListItem.BusRoute(it) }
                ))
            }
            transportAdapter.update(sections)
        }
    }
}