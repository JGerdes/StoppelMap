package com.jonasgerdes.stoppelmap.map

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.map.entity.Stall
import com.jonasgerdes.stoppelmap.util.inflate
import kotlinx.android.synthetic.main.map_stall_card.view.*


class StallCardAdapter: ListAdapter<Stall, StallCardAdapter.StallCardHolder>(Stall.DiffCallback) {

    fun itemAt(position: Int) = getItem(position)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StallCardHolder {
        return StallCardHolder(parent.inflate(R.layout.map_stall_card))
    }

    override fun onBindViewHolder(holder: StallCardHolder, position: Int) {
        holder.bind(getItem(position))
    }


    class StallCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(stall: Stall) {
            itemView.title.text = stall.name
        }

    }
}