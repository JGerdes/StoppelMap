package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.view.View
import com.jonasgerdes.stoppelmap.model.entity.map.detail.EntityProductCard
import kotlinx.android.synthetic.main.map_entity_card_products.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
class EntityProductCardHolder(itemView: View)
    : EntityCardHolder<EntityProductCard>(itemView) {

    init {
        itemView.products.adapter = ProductAdapter()
    }

    override fun onBind(card: EntityProductCard) {
        (itemView.products.adapter as ProductAdapter).products = card.products
    }

}