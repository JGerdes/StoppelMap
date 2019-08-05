package com.jonasgerdes.stoppelmap.map.view

import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.entity.Highlight
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_stall_carousel.view.*

abstract class CarouselItem : Item() {
    abstract val highlight: Highlight
}

data class StallCarouselItem(
    override val highlight: Highlight.SingleStall
) : CarouselItem() {

    override fun getLayout() = R.layout.item_stall_carousel

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = highlight.stall.name ?: highlight.stall.type.name
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is StallCarouselItem) {
            return false
        }
        return other.highlight.stall.slug == highlight.stall.slug
    }
}