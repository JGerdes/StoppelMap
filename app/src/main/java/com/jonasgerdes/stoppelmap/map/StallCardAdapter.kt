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
import com.jonasgerdes.stoppelmap.util.*
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
                    val cardTitle = stallCard.stall.name ?: stallCard.type.name
                    title.text = cardTitle
                    card.setStallTypeBackgroundColor(stallCard.stall.type)
                    type.setTextOrHide(stallCard.subTypes
                            .distinctBy { it.slug }
                            .map { it.name }
                            .filter { it != cardTitle }
                            .joinToString(", "))
                    val header = stallCard.images.headers().firstOrNull()
                    val imagePath = stallCard.stall.getImagePath(header)
                    GlideApp.with(cardBackground)
                            .load(imagePath)
                            .centerCrop()
                            .into(cardBackground)

                    items.setTextOrHide(when (stallCard.stall.type) {
                        Type.FOOD_STALL, Type.CANDY_STALL, Type.SELLER_STALL,
                        Type.BAR, Type.BUILDING -> stallCard.items
                                .distinctBy { it.slug }
                                .filter { it.name != cardTitle }
                                .joinToString(", ") { it.name }.let {
                                    if (it.isNotEmpty()) {
                                        it + ", " + context.getString(R.string.generic_enumeration_more_suffix)
                                    } else null
                                }
                        Type.GAME_STALL -> stallCard.items
                                .distinctBy { it.slug }
                                .filter { it.name != cardTitle }
                                .joinToString(", ") { it.name }

                        else -> null
                    })
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