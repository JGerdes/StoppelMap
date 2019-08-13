package com.jonasgerdes.stoppelmap.transport.view.route

import com.jonasgerdes.stoppelmap.transport.R
import com.jonasgerdes.stoppelmap.transport.usecase.GetFullRouteUseCase
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_transport_station.view.*

class DestinationStationItem() : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            itemView.title.setText(R.string.transport_destination_station_title)
        }
    }

    override fun getLayout() = R.layout.item_transport_station
}