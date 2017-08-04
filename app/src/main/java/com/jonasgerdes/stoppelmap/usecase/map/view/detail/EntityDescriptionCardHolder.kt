package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.view.View
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.detail.EntityDescriptionCard
import kotlinx.android.synthetic.main.map_entity_card_description.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
class EntityDescriptionCardHolder(itemView: View)
    : EntityCardHolder<EntityDescriptionCard>(itemView) {

    override fun onBind(card: EntityDescriptionCard) {
        itemView.descriptionText.text = card.description
        if (card.source != null) {
            itemView.source.visibility = View.VISIBLE
            itemView.source.text = itemView.context.getString(
                    R.string.entity_detail_card_description_source, card.source)
        }
    }

}