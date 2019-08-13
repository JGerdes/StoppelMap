package com.jonasgerdes.stoppelmap.transport.view.route

import com.jonasgerdes.stoppelmap.transport.R
import com.jonasgerdes.stoppelmap.transport.usecase.GetFullRouteUseCase
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_transport_station.view.*
import org.threeten.bp.format.DateTimeFormatter

private val TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm")

data class StationItem(val stationInformation: GetFullRouteUseCase.StationInformation) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            itemView.title.text = stationInformation.basicInfo.name
            itemView.nextDepartures.text = if (stationInformation.nextDepartures.isEmpty())
                itemView.context.getString(R.string.transport_no_next_departures)
            else stationInformation.nextDepartures.joinToString("\n") { time ->
                itemView.context.getString(R.string.transport_next_departure_time, time.format(TIME_FORMAT))
            }
        }
    }

    override fun getLayout() = R.layout.item_transport_station
}