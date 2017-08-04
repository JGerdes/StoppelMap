package com.jonasgerdes.stoppelmap.usecase.map.view.search

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.map_entity_card_phone_numbers_number.view.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 04.08.17
 */
class PhoneNumberHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun onBind(name: String, prettifiedNumber: String?, description: String?) {
        itemView.title.text = name

        if (prettifiedNumber != null) {
            itemView.phoneNumber.text = prettifiedNumber
            itemView.phoneNumber.visibility = View.VISIBLE
        } else {
            itemView.phoneNumber.visibility = View.GONE
        }

        if (description != null) {
            itemView.subTitle.text = description
            itemView.subTitle.visibility = View.VISIBLE
        } else {
            itemView.subTitle.visibility = View.GONE
        }
    }

}