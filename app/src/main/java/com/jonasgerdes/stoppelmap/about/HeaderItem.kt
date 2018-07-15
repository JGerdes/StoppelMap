package com.jonasgerdes.stoppelmap.about

import com.jonasgerdes.stoppelmap.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_item_header.*

data class HeaderItem(
        val title: String
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = title
    }

    override fun getLayout() = R.layout.about_item_header

}



