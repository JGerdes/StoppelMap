package com.jonasgerdes.stoppelmap.transport.view.route

import com.jonasgerdes.stoppelmap.transport.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_transport_destination_station.view.*
import kotlinx.android.synthetic.main.item_transport_station.view.title

class DestinationStationItem(private val onReturnRouteClicked: () -> Unit) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            itemView.title.setText(R.string.transport_destination_station_title)
            itemView.openReturnRoute.setOnClickListener {
                onReturnRouteClicked()
            }
        }
    }

    override fun getLayout() = R.layout.item_transport_destination_station
}