package com.jonasgerdes.stoppelmap.map.view

import com.jonasgerdes.stoppelmap.map.R
import com.jonasgerdes.stoppelmap.map.entity.Highlight
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_stall_carousel.view.title
import kotlinx.android.synthetic.main.item_type_collection_carousel.view.*

abstract class CarouselItem : Item() {
    abstract val highlight: Highlight
}

data class StallCarouselItem(
    override val highlight: Highlight.SingleStall
) : CarouselItem() {

    override fun getLayout() = R.layout.item_stall_carousel

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = highlight.stall.basicInfo.name
            subtitle.text = if (highlight.stall.subTypes.isNotEmpty()) {
                highlight.stall.subTypes.joinToString(",\n") { it.name }
            } else {
                context.getTypeName(highlight.stall.basicInfo.type)
            }
        }
    }
}


data class TypeCollectionCarouselItem(
    override val highlight: Highlight.TypeCollection,
    val isOnlyOne: Boolean
) : CarouselItem() {

    override fun getLayout() = R.layout.item_type_collection_carousel

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            val text = if (isOnlyOne) R.string.map_carousel_item_more else R.string.map_carousel_item_and_more
            subtitle.text = context.getString(text, highlight.stalls.size)
            title.text = highlight.type.name
        }
    }
}

data class ItemCollectionCarouselItem(
    override val highlight: Highlight.ItemCollection,
    val isOnlyOne: Boolean
) : CarouselItem() {

    override fun getLayout() = R.layout.item_type_collection_carousel

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            val text = if (isOnlyOne) R.string.map_carousel_item_more else R.string.map_carousel_item_and_more
            subtitle.text = context.getString(text, highlight.stalls.size)
            title.text = highlight.item.name
        }
    }
}

data class NamelessStallCarouselItem(
    override val highlight: Highlight.NamelessStall
) : CarouselItem() {

    override fun getLayout() = R.layout.item_generic_stall_carousel

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = context.getTypeName(highlight.stall.basicInfo.type)
        }
    }
}