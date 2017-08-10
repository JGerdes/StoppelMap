package com.jonasgerdes.stoppelmap.usecase.transportation.route_detail.view

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Station
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.transportation_stations_list_item.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.17
 */
class StationAdapter : RecyclerView.Adapter<StationHolder>() {

    private var stationList: List<Station> = ArrayList()
    private val selectedSubject = BehaviorSubject.create<Station>()

    var stations
        get() = stationList
        set(value) {
            stationList = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when {
            position >= stationList.size -> R.layout.transportation_stations_list_item_destination
            else -> R.layout.transportation_stations_list_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): StationHolder {
        val view = LayoutInflater.from(parent?.context).inflate(viewType, parent, false)
        val holder = StationHolder(view)
        if (viewType == R.layout.transportation_stations_list_item) {
            view.details.onClick {
                selectedSubject.onNext(stationList[holder.adapterPosition])
            }
            view.onClick {
                selectedSubject.onNext(stationList[holder.adapterPosition])
            }
        }
        return holder
    }

    override fun onBindViewHolder(holder: StationHolder, position: Int) {
        if (getItemViewType(position) == R.layout.transportation_stations_list_item) {
            holder.onBind(stationList[position], position == 0)
        }
    }

    override fun getItemCount(): Int {
        return stationList.size + 1
    }

    fun selections(): Observable<Station> {
        return selectedSubject.hide()
    }
}