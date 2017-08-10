package com.jonasgerdes.stoppelmap.usecase.transportation.station_detail.view.departure_day

import android.support.v7.widget.RecyclerView
import android.view.View
import com.jonasgerdes.stoppelmap.model.entity.Departure
import kotlinx.android.synthetic.main.transportation_station_detail_departure.view.*
import java.text.SimpleDateFormat

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.17
 */
class DepartureHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val FORMAT_TIME = SimpleDateFormat("kk:mm")

    fun onBind(departure: Departure) {
        with(itemView) {
            time.text = FORMAT_TIME.format(departure.time)
        }
    }
}