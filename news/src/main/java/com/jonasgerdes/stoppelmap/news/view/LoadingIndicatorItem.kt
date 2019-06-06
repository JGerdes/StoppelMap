package com.jonasgerdes.stoppelmap.news.view

import com.jonasgerdes.stoppelmap.news.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class LoadingIndicatorItem() : Item() {

    override fun getLayout() = R.layout.item_loading_indicator

    override fun bind(viewHolder: ViewHolder, position: Int) {}

    override fun isSameAs(other: com.xwray.groupie.Item<*>?) = other !is LoadingIndicatorItem

}