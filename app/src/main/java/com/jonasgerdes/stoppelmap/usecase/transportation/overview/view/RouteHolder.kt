package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.DepartureDay
import com.jonasgerdes.stoppelmap.model.entity.Route
import kotlinx.android.synthetic.main.transportation_overview_route.view.*
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.17
 */
class RouteHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val FORMAT_NEXT_TIME = SimpleDateFormat("kk:mm")

    fun onBind(route: Route) {
        itemView.name.text = route.name

        val nextStation = route.stations!!.first()
        val departure = nextStation.getNextDepature(Calendar.getInstance())
        if (departure != null) {
            itemView.nextStation.text = String.format("ab %s", nextStation.name)
            itemView.nextStation.visibility = View.VISIBLE
            itemView.nextTime.visibility = View.VISIBLE
            val timeString = FORMAT_NEXT_TIME.format(departure.time)
            val departureDay = departure.getDay();
            val todaysDay = DepartureDay.getDayFromCalendar(Calendar.getInstance())
            val departureString = if (departureDay == todaysDay) {
                String.format("um %s Uhr ab", timeString);
            } else {
                val dayPrefix = itemView.context.resources.getStringArray(R.array.days)[departureDay]
                String.format("%s um %s Uhr", dayPrefix, timeString);
            }
            itemView.nextTime.text = departureString;

        } else {
            itemView.nextStation.visibility = View.INVISIBLE
            itemView.nextTime.setText(R.string.transportation_overview_route_card_no_departures)
        }
    }

}