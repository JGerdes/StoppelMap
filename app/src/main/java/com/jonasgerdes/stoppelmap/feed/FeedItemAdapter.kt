package com.jonasgerdes.stoppelmap.feed

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.jakewharton.rxbinding2.view.clicks
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.news.FeedItemWithImages
import com.jonasgerdes.stoppelmap.util.GlideApp
import com.jonasgerdes.stoppelmap.util.inflate
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.feed_list_item_image.view.*
import org.threeten.bp.format.DateTimeFormatter

class FeedItemAdapter : ListAdapter<FeedItemWithImages, FeedItemAdapter.Holder>(FeedItemWithImages.Diff) {

    private val dateFormat = DateTimeFormatter.ofPattern("dd. MMMM yyyy")

    val itemClicks = PublishSubject.create<FeedItemWithImages>()

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
        holder.itemView.clicks()
                .map { getItem(position) }
                .subscribe(itemClicks)
        holder.itemView.actionMore.clicks()
                .map { getItem(position) }
                .subscribe(itemClicks)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: FeedItemWithImages) {
            val context = itemView.context
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
                    itemView.copyright.text = context.getString(
                            R.string.feed_card_photo_copyright,
                            image.author
                    )
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