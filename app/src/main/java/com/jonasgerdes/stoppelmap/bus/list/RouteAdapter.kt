package com.jonasgerdes.stoppelmap.bus.list

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.domain.model.TransportRoute
import com.jonasgerdes.stoppelmap.util.inflate
import kotlinx.android.synthetic.main.bus_list_route_card.view.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class RouteAdapter : ListAdapter<TransportRoute, RouteAdapter.Holder>(TransportRoute.DiffCallback) {

    val format = DateTimeFormatter.ofPattern("HH:mm:ss")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(parent.inflate(R.layout.bus_list_route_card))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(route: TransportRoute) {
            itemView.title.text = route.name
            itemView.stations.text = route.nearestStations.map { it.name }.joinToString("\n")
            itemView.departures.text = route.nearestStations.map {
                if (it.timeUntilDeparture < TimeUnit.SECONDS.toMillis(90)) {
                    itemView.context.getString(R.string.transportation_relative_time_immediately)
                } else {
                    DateUtils.getRelativeTimeSpanString(it.timeUntilDeparture, 0, 0, DateUtils.FORMAT_ABBREV_RELATIVE)
                            .toString().decapitalize()
                }
            }.joinToString("\n")
        }
    }
}