package com.jonasgerdes.stoppelmap.map

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.map.SingleStallCard
import com.jonasgerdes.stoppelmap.model.map.StallCard
import com.jonasgerdes.stoppelmap.model.map.entity.headers
import com.jonasgerdes.stoppelmap.util.*
import kotlinx.android.synthetic.main.map_stall_card.view.*


class StallCardAdapter : ListAdapter<StallCard, StallCardAdapter.StallCardHolder>(StallCard.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StallCardHolder {
        return StallCardHolder(parent.inflate(R.layout.map_stall_card))
    }

    override fun onBindViewHolder(holder: StallCardHolder, position: Int) {
        val item = getItem(position)
        when (item) {
            is SingleStallCard -> holder.bindSingleStall(item)
        }
    }


    class StallCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindSingleStall(card: SingleStallCard) {
            itemView.title.text = card.stall.name
            itemView.card.setStallTypeBackgroundColor(card.stall.type)
            GlideApp.with(itemView.cardBackground)
                    .load(card.stall.getImagePath(card.images.headers().firstOrNull()).apply { Log.d("StallCardAdapter", this) })
                    .centerCrop()
                    .into(itemView.cardBackground)
        }

    }
}