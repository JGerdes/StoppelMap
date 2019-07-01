package com.jonasgerdes.stoppelmap.news.view

import android.widget.TextView
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.jonasgerdes.androidutil.asRelativeString
import com.jonasgerdes.androidutil.recyclerview.LinePagerIndicatorDecoration
import com.jonasgerdes.androidutil.recyclerview.OnCurrentItemChangedListener
import com.jonasgerdes.androidutil.recyclerview.removeAllItemDecorations
import com.jonasgerdes.stoppelmap.news.R
import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_article_no_images.view.date
import kotlinx.android.synthetic.main.item_article_no_images.view.teaser
import kotlinx.android.synthetic.main.item_article_no_images.view.title
import kotlinx.android.synthetic.main.item_article_with_images.view.*


data class ArticleWithImagesItem(
    override val article: Article,
    val onMoreClickedListener: (article: Article) -> Unit
) : ArticleItem() {

    private val pagerIndicator = LinePagerIndicatorDecoration()
    private var onCurrentItemChangedListener: OnCurrentItemChangedListener? = null

    override fun getLayout() = R.layout.item_article_with_images

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = article.title
            teaser.text = article.teaser
            date.text = article.publishDate.asRelativeString(context.resources)
            copyright.updateCopyright(article.images[0].author)

            images.adapter = GroupAdapter<com.xwray.groupie.ViewHolder>().apply {
                addAll(article.images.map {
                    ImageItem(it)
                })
            }

            onCurrentItemChangedListener?.let { images.removeOnScrollListener(it) }
            images.clearOnScrollListeners() //sometimes onCurrentItemChangedListener isn't remove by line above
            images.removeItemDecoration(pagerIndicator)
            images.removeAllItemDecorations() //sometimes pagerIndicator isn't remove by line above
            //TODO: investigate why itemDecorator and scrollListener aren't removed properly

            images.onFlingListener = null
            PagerSnapHelper().attachToRecyclerView(images)


            if (article.images.size > 1) {
                images.addItemDecoration(pagerIndicator)
                onCurrentItemChangedListener = OnCurrentItemChangedListener { currentItem ->
                    copyright.updateCopyright(article.images[currentItem].author)
                }.also {
                    images.addOnScrollListener(it)
                }
            }

            images.overScrollMode = if (article.images.size > 1) RecyclerView.OVER_SCROLL_ALWAYS
            else RecyclerView.OVER_SCROLL_NEVER

            moreButton.setOnClickListener {
                onMoreClickedListener(article)
            }
        }
    }

    private fun TextView.updateCopyright(author: String?) {
        text = if (author == null) "" else context.getString(R.string.news_card_image_copyright, author)
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ArticleWithImagesItem) {
            return false
        }
        return other.article.url == article.url
    }

}