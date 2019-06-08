package com.jonasgerdes.stoppelmap.news.view

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.jonasgerdes.stoppelmap.news.R
import com.jonasgerdes.stoppelmap.news.data.entity.Image
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_image.view.*

data class ImageItem(
    val image: Image
) : Item() {

    override fun getLayout() = R.layout.item_image

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            Glide.with(context)
                .load(image.url)
                .into(imageView)
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ImageItem) {
            return false
        }
        return other.image.url == image.url
    }

}