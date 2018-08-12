package com.jonasgerdes.stoppelmap.bus.list

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.domain.model.TransportRoute
import com.jonasgerdes.stoppelmap.util.inflate
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.bus_list_route_card.view.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class RouteAdapter : ListAdapter<TransportRoute, RouteAdapter.Holder>(TransportRoute.DiffCallback) {

    sealed class RouteClickedEvent {
        data class Card(val route: TransportRoute) : RouteClickedEvent()
        data class AllStations(val route: TransportRoute) : RouteClickedEvent()
        data class Return(val route: TransportRoute) : RouteClickedEvent()
    }

    val format = DateTimeFormatter.ofPattern("HH:mm:ss")
    private val eventSubject = PublishRelay.create<RouteClickedEvent>()
    val events = eventSubject.hide()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(parent.inflate(R.layout.bus_list_route_card)).apply {
                itemView.actionAllStations.clicks()
                        .map { RouteClickedEvent.AllStations(getItem(adapterPosition)) }
                        .subscribe(eventSubject)
                itemView.actionReturn.clicks()
                        .map { RouteClickedEvent.Return(getItem(adapterPosition)) }
                        .subscribe(eventSubject)
                itemView.clicks()
                        .map { RouteClickedEvent.Card(getItem(adapterPosition)) }
                        .subscribe(eventSubject)
            }

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