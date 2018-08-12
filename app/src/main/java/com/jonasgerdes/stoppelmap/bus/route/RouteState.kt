package com.jonasgerdes.stoppelmap.bus.route

import android.support.v7.util.DiffUtil

data class RouteState(
        val stations: List<TransportStation>
)

data class TransportStation(
        val slug: String,
        val name: String,
        val nextDepartures: List<Long>
) {

    object DiffCallback : DiffUtil.ItemCallback<TransportStation>() {
        override fun areItemsTheSame(old: TransportStation, new: TransportStation) = old.slug == new.slug

        override fun areContentsTheSame(old: TransportStation, new: TransportStation) = old == new

    }

    companion object {
        val STOPPELMARKT = TransportStation("-", "Stoppelmarkt", emptyList())
    }
}