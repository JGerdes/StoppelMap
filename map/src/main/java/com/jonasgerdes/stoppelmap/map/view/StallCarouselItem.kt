package com.jonasgerdes.stoppelmap.map.view

import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.model.map.Stall
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_stall_carousel.view.*


data class StallCarouselItem(
    val stall: Stall
) : Item() {

    override fun getLayout() = R.layout.item_stall_carousel

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = stall.name ?: stall.type.name
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is StallCarouselItem) {
            return false
        }
        return other.stall.slug == stall.slug
    }
}