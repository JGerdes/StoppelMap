package com.jonasgerdes.stoppelmap.domain.model

import android.support.v7.util.DiffUtil
import org.threeten.bp.LocalDateTime


data class TransportRoute(
        val slug: String,
        val name: String,
        val nearestStations: List<Station>
) {
    data class Station(
            val name: String,
            val timeUntilDeparture: Long
    )

    object DiffCallback : DiffUtil.ItemCallback<TransportRoute>() {
        override fun areItemsTheSame(old: TransportRoute, new: TransportRoute) = old.slug == new.slug

        override fun areContentsTheSame(old: TransportRoute, new: TransportRoute) = old == new

    }
}