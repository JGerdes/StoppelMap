package com.jonasgerdes.stoppelmap.bus.route

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxrelay2.PublishRelay
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.inflate
import kotlinx.android.synthetic.main.bus_route_station_card.view.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

class StationAdapter : ListAdapter<TransportStation, StationAdapter.Holder>(TransportStation.DiffCallback) {

    sealed class StationClickedEvent {
        data class Card(val station: TransportStation) : StationClickedEvent()
        data class AllDepartures(val station: TransportStation) : StationClickedEvent()
    }

    val format = DateTimeFormatter.ofPattern("HH:mm:ss")
    private val eventSubject = PublishRelay.create<StationClickedEvent>()
    val events = eventSubject.hide()

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            itemCount - 1 -> R.layout.bus_route_station_card_final
            else -> R.layout.bus_route_station_card
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
            Holder(parent.inflate(viewType)).apply {
                itemView.clicks()
                        .map { StationClickedEvent.Card(getItem(adapterPosition)) }
                        .subscribe(eventSubject)
                if (viewType == R.layout.bus_route_station_card) {
                    itemView.actionAllDepartures.clicks()
                            .map { StationClickedEvent.AllDepartures(getItem(adapterPosition)) }
                            .subscribe(eventSubject)
                }

            }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.bus_route_station_card -> holder.bindStation(getItem(position), position == 0)
            R.layout.bus_route_station_card_final -> holder.bindFinalStation(getItem(position))
        }

    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindFinalStation(station: TransportStation) {
            itemView.title.text = station.name
        }

        fun bindStation(station: TransportStation, isFirst: Boolean) {
            itemView.line.setBackgroundResource(if (isFirst) {
                R.drawable.item_route_line_top
            } else {
                R.drawable.item_route_line
            })
            itemView.title.text = station.name
            itemView.departures.text = station.nextDepartures.map {
                if (it < TimeUnit.SECONDS.toMillis(90)) {
                    itemView.context.getString(R.string.transportation_relative_time_immediately)
                } else {
                    DateUtils.getRelativeTimeSpanString(it, 0, 0, DateUtils.FORMAT_ABBREV_RELATIVE)
                            .toString().decapitalize()
                }
            }.joinToString("\n")
        }
    }
}