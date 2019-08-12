package com.jonasgerdes.stoppelmap.transport.view

import com.jonasgerdes.stoppelmap.transport.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_transport_bus_route.view.*

sealed class TransportListItem : Item() {

    data class BusRoute(val busRoute: com.jonasgerdes.stoppelmap.transport.entity.BusRoute) : TransportListItem() {
        override fun bind(viewHolder: ViewHolder, position: Int) {
            viewHolder.apply {
                itemView.title.text = busRoute.title
                itemView.stations.text = busRoute.via.joinToString("\n")
            }
        }

        override fun getLayout() = R.layout.item_transport_bus_route
    }
}


data class CategoryTitle(val title: String) : TransportListItem() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            itemView.title.text = title
        }
    }

    override fun getLayout() = R.layout.item_transport_title
}