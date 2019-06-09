package com.jonasgerdes.stoppelmap.news.view

import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.jonasgerdes.androidutil.navigation.onComplete
import com.jonasgerdes.androidutil.navigation.startIfAnimatable
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
            loadingIndicator.setImageResource(R.drawable.ic_anim_photo_loading)
            loadingIndicator.drawable.startIfAnimatable()
            errorText.isVisible = false
            scrim.isVisible = false
            Glide.with(context)
                .load(image.url)
                .onComplete { success ->
                    if (success) {
                        scrim.isVisible = true
                    } else {
                        loadingIndicator.setImageResource(R.drawable.ic_photo_error)
                        errorText.isVisible = true
                    }
                }
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