package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.view.View
import com.jonasgerdes.stoppelmap.model.entity.map.detail.EntityPhoneNumberCard
import kotlinx.android.synthetic.main.map_entity_card_phone_numbers.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
class EntityPhoneCardHolder(itemView: View)
    : EntityCardHolder<EntityPhoneNumberCard>(itemView) {

    init {
        itemView.phoneNumbers.adapter = PhoneNumberAdapter()
    }

    override fun onBind(card: EntityPhoneNumberCard) {
        (itemView.phoneNumbers.adapter as PhoneNumberAdapter).phoneNumbers = card.phoneNumbers
    }

}