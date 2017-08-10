package com.jonasgerdes.stoppelmap.usecase.transportation.station_detail.view.departure_day

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Departure

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.17
 */
class DepartureAdapter : RecyclerView.Adapter<DepartureHolder>() {

    private var departureList: List<Departure> = ArrayList()

    var departures
        get() = departureList
        set(value) {
            departureList = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): DepartureHolder {
        val view = LayoutInflater.from(parent?.context).inflate(
                R.layout.transportation_station_detail_departure, parent, false)
        return DepartureHolder(view)
    }

    override fun onBindViewHolder(holder: DepartureHolder, position: Int) {
        holder.onBind(departureList[position])
    }

    override fun getItemCount(): Int {
        return departureList.size
    }
}