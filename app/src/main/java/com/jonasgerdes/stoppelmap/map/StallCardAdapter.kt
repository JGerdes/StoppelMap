package com.jonasgerdes.stoppelmap.map

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.map.SingleStallCard
import com.jonasgerdes.stoppelmap.model.map.StallCard
import com.jonasgerdes.stoppelmap.model.map.StallCollectionCard
import com.jonasgerdes.stoppelmap.model.map.entity.Type
import com.jonasgerdes.stoppelmap.model.map.entity.headers
import com.jonasgerdes.stoppelmap.util.GlideApp
import com.jonasgerdes.stoppelmap.util.getImagePath
import com.jonasgerdes.stoppelmap.util.inflate
import com.jonasgerdes.stoppelmap.util.setStallTypeBackgroundColor
import kotlinx.android.synthetic.main.map_stall_card_single_stall.view.*
import kotlinx.android.synthetic.main.map_stall_card_stall_collection.view.*


class StallCardAdapter : ListAdapter<StallCard, StallCardAdapter.StallCardHolder>(StallCard.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StallCardHolder {
        val view = parent.inflate(viewType)
        return when (viewType) {
            R.layout.map_stall_card_single_stall -> StallCardHolder.Single(view)
            R.layout.map_stall_card_stall_collection -> StallCardHolder.Collection(view)
            else -> throw IllegalArgumentException("Invalid $viewType as StallCard")
        }
    }

    override fun onBindViewHolder(holder: StallCardHolder, position: Int) {
        val item = getItem(position)
        when (holder) {
            is StallCardHolder.Single -> holder.bind(item as SingleStallCard)
            is StallCardHolder.Collection -> holder.bind(item as StallCollectionCard)
        }
    }

    override fun getItemViewType(position: Int) =
            when (getItem(position)) {
                is SingleStallCard -> R.layout.map_stall_card_single_stall
                is StallCollectionCard -> R.layout.map_stall_card_stall_collection
            }


    sealed class StallCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        class Single(itemView: View) : StallCardHolder(itemView) {
            fun bind(stallCard: SingleStallCard) {
                itemView.apply {
                    title.text = stallCard.stall.name
                    card.setStallTypeBackgroundColor(stallCard.stall.type)
                    var subtypes = stallCard.subTypes.map { it.name }
                    if (stallCard.stall.type == Type.GAME_STALL) {
                        subtypes += stallCard.items.map { it.name }
                    }
                    type.text = subtypes.joinToString(", ")
                    val header = stallCard.images.headers().firstOrNull()
                    GlideApp.with(cardBackground)
                            .load(stallCard.stall.getImagePath(header))
                            .centerCrop()
                            .into(cardBackground)
                }
            }
        }

        class Collection(itemView: View) : StallCardHolder(itemView) {
            fun bind(card: StallCollectionCard) {
                itemView.collectionTitle.text = card.title
                itemView.text.text = if (card.areAdditional) {
                    "${card.stalls.size} weitere auf dem Stoppelmarkt"
                } else {
                    "${card.stalls.size} mal auf dem Stoppelmarkt"
                }
                itemView.collectionCard.setStallTypeBackgroundColor(card.type)
            }
        }
    }
}