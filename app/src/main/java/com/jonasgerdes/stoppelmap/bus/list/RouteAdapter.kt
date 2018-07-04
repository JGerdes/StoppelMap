package com.jonasgerdes.stoppelmap.bus.list

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.transportation.Route
import com.jonasgerdes.stoppelmap.util.inflate
import kotlinx.android.synthetic.main.bus_list_route_card.view.*

class RouteAdapter: ListAdapter<Route, RouteAdapter.Holder>(Route.DiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Holder(parent.inflate(R.layout.bus_list_route_card))

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    class Holder(itemView: View):RecyclerView.ViewHolder(itemView) {
        fun bind(route: Route) {
            itemView.title.text = route.name
        }
    }
}