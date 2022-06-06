package com.jonasgerdes.stoppelmap.home.view.item

import android.text.method.LinkMovementMethod
import com.jonasgerdes.stoppelmap.core.util.fromHtml
import com.jonasgerdes.stoppelmap.home.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_home_regulations.view.*
import kotlinx.android.synthetic.main.item_home_title_and_description_card.view.*

object RegulationsItem : Item() {
    override fun getLayout() = R.layout.item_home_regulations

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            openingHours.text =
                context.getString(R.string.home_card_opening_hours_description).fromHtml()
            formLink.text = context.getString(R.string.home_card_regulations_form).fromHtml()
            formLink.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?) = other is RegulationsItem
}