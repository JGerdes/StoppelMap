package com.jonasgerdes.stoppelmap.usecase.transportation.route_detail.view

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.DepartureDay
import com.jonasgerdes.stoppelmap.model.entity.Station
import kotlinx.android.synthetic.main.transportation_stations_list_item.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.17
 */
class StationHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val FORMAT_NEXT_TIME = SimpleDateFormat("kk:mm")

    fun onBind(station: Station, isFirst: Boolean) {
        with(itemView) {
            name.text = station.name

            nodeTop.visibility = if (isFirst) {
                View.GONE
            } else {
                View.VISIBLE
            }

            val departures = listOf(depature1, depature2, depature3)
            departures.forEach { it.visibility = View.GONE }

            val now = Calendar.getInstance()
            val today = DepartureDay.getDayFromCalendar(now)
            val nextDepartures = station.getNextDepatures(now, 3)

            nextDepartures.map {
                val timeString = FORMAT_NEXT_TIME.format(it.time)
                if (it.getDay() == today) {
                    String.format("%s Uhr", timeString)
                } else {
                    val dayPrefix = context.resources.getStringArray(R.array.days)[it.getDay()]
                    String.format("%s, %s Uhr", dayPrefix, timeString)
                }
            }.forEachIndexed { index, text ->
                departures[index].text = text
                departures[index].visibility = visibility
            }

            if (nextDepartures.isEmpty()) {
                depature1.visibility = View.VISIBLE
                depature1.setText(R.string.transportation_overview_route_card_no_departures);
            }
        }
    }
}