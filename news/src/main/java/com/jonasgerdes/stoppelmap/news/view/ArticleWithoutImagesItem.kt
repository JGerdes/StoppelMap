package com.jonasgerdes.stoppelmap.news.view

import com.jonasgerdes.androidutil.navigation.asRelativeString
import com.jonasgerdes.stoppelmap.news.R
import com.jonasgerdes.stoppelmap.news.data.entity.Article
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_article_no_images.view.*

data class ArticleWithoutImagesItem(
    val article: Article
) : Item() {

    override fun getLayout() = R.layout.item_article_no_images

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = article.title
            teaser.text = article.teaser
            date.text = article.publishDate.asRelativeString(context.resources)
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ArticleWithoutImagesItem) {
            return false
        }
        return other.article.url == article.url
    }

}