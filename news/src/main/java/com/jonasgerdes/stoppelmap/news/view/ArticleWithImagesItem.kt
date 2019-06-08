package com.jonasgerdes.stoppelmap.news.view

import androidx.recyclerview.widget.PagerSnapHelper
import com.jonasgerdes.androidutil.navigation.asRelativeString
import com.jonasgerdes.androidutil.navigation.recyclerview.LinePagerIndicatorDecoration
import com.jonasgerdes.androidutil.navigation.recyclerview.removeAllItemDecorations
import com.jonasgerdes.stoppelmap.news.R
import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_article_no_images.view.date
import kotlinx.android.synthetic.main.item_article_no_images.view.teaser
import kotlinx.android.synthetic.main.item_article_no_images.view.title
import kotlinx.android.synthetic.main.item_article_with_images.view.*


data class ArticleWithImagesItem(
    val article: Article
) : Item() {

    private val pagerIndicator = LinePagerIndicatorDecoration()

    override fun getLayout() = R.layout.item_article_with_images

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = article.title
            teaser.text = article.teaser
            date.text = article.publishDate.asRelativeString(context.resources)
            images.adapter = GroupAdapter<com.xwray.groupie.ViewHolder>().apply {
                addAll(article.images.map {
                    ImageItem(it)
                })
            }
            images.onFlingListener = null
            PagerSnapHelper().attachToRecyclerView(images)

            images.invalidateItemDecorations()
            if (article.images.size > 1) {
                images.addItemDecoration(pagerIndicator)
            } else {
                images.removeItemDecoration(pagerIndicator)
                images.removeAllItemDecorations() //sometimes pagerIndicator isn't remove by line above
            }
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ArticleWithImagesItem) {
            return false
        }
        return other.article.url == article.url
    }

}