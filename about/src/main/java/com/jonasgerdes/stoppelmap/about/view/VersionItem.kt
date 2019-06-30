package com.jonasgerdes.stoppelmap.about.view

import com.jonasgerdes.stoppelmap.about.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_item_version.view.*

data class VersionItem(
    val versionName: String
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.version.text = versionName
    }

    override fun getLayout() = R.layout.about_item_version

}



