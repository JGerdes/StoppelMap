package com.jonasgerdes.stoppelmap.about.view

import com.jonasgerdes.stoppelmap.about.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_item_header.view.*

data class HeaderItem(
    val title: String
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.title.text = title
    }

    override fun getLayout() = R.layout.about_item_header

}



