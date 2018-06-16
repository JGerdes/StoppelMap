package com.jonasgerdes.stoppelmap.feed

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.news.FeedItemWithImages
import com.jonasgerdes.stoppelmap.util.GlideApp
import com.jonasgerdes.stoppelmap.util.inflate
import com.mapbox.mapboxsdk.style.layers.Property
import kotlinx.android.synthetic.main.feed_list_item_image.view.*
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat

class FeedItemAdapter : ListAdapter<FeedItemWithImages, FeedItemAdapter.Holder>(FeedItemWithImages.Diff) {

    private val dateTimeFormat = DateTimeFormatter.ofPattern("dd. MMMM yyyy - HH:mm")
    private val dateFormat = DateTimeFormatter.ofPattern("dd. MMMM yyyy")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent.inflate(viewType))
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).images!!.isEmpty()) {
            R.layout.feed_list_item
        } else {
            R.layout.feed_list_item_image
        }
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FeedItemWithImages) {
            if (item.feedItem?.subTitle == null) {
                itemView.title.text = item.feedItem?.title
                itemView.subtitle.text = ""
            } else {
                itemView.title.text = item.feedItem?.subTitle
                itemView.subtitle.text = item.feedItem?.title
            }
            itemView.content.text = item.feedItem?.content
            itemView.date.text = item.feedItem?.publishDate?.format(dateFormat)

            if (item.images!!.isNotEmpty()) {
                val image = item.images!!.first()
                if (image.author != null) {
                    itemView.copyright.visibility = View.VISIBLE
                    itemView.copyright.text = image.author
                } else {
                    itemView.copyright.visibility = View.GONE
                }
                GlideApp.with(itemView)
                        .load(image.url)
                        .transition(withCrossFade())
                        .centerCrop()
                        .into(itemView.image)
            }

        }
    }
}