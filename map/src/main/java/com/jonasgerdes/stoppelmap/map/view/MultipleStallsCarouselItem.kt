package com.jonasgerdes.stoppelmap.map.view

import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.model.map.Stall
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_stall_carousel.view.*


data class MultipleStallsCarouselItem(
    val stalls: List<Stall>
) : Item() {

    override fun getLayout() = R.layout.item_stall_carousel

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = "$stalls.size"
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is MultipleStallsCarouselItem) {
            return false
        }
        return other.stalls == stalls
    }
}