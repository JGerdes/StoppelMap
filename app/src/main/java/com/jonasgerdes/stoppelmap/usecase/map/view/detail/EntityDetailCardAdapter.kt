package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.detail.EntityDescriptionCard
import com.jonasgerdes.stoppelmap.model.entity.map.detail.EntityDetailCard
import com.jonasgerdes.stoppelmap.model.entity.map.detail.EntityProductCard

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
class EntityDetailCardAdapter : RecyclerView.Adapter<EntityCardHolder<*>>() {

    private var cardList: List<EntityDetailCard> = ArrayList()

    var cards
        get() = cardList
        set(value) {
            cardList = value
            notifyDataSetChanged()
        }

    override fun getItemViewType(position: Int): Int {
        return when (cardList[position]) {
            is EntityProductCard -> R.layout.map_entity_card_products
            is EntityDescriptionCard -> R.layout.map_entity_card_description
            else -> -1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EntityCardHolder<*> {
        val view = LayoutInflater.from(parent?.context).inflate(viewType, parent, false)
        val holder = when (viewType) {
            R.layout.map_entity_card_products -> EntityProductCardHolder(view)
            R.layout.map_entity_card_description -> EntityDescriptionCardHolder(view)
            else -> EntityProductCardHolder(view)
        }
        return holder
    }

    override fun onBindViewHolder(holder: EntityCardHolder<*>?, position: Int) {
        when (holder) {
            is EntityProductCardHolder
            -> holder.onBind(cardList[position] as EntityProductCard)

            is EntityDescriptionCardHolder
            -> holder.onBind(cardList[position] as EntityDescriptionCard)
        }
    }

    override fun getItemCount(): Int {
        return cardList.size
    }
}