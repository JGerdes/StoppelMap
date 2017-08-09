package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Route
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.17
 */
class RouteAdapter : RecyclerView.Adapter<RouteHolder>() {

    private var routeList: List<Route> = ArrayList()
    private val selectedSubject = BehaviorSubject.create<Route>()

    var routes
        get() = routeList
        set(value) {
            routeList = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RouteHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.transportation_overview_route, parent, false)
        val holder = RouteHolder(view)
        view.onClick {
            selectedSubject.onNext(routeList[holder.adapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: RouteHolder, position: Int) {
        holder.onBind(routeList[position])
    }

    override fun getItemCount(): Int {
        return routeList.size
    }
}