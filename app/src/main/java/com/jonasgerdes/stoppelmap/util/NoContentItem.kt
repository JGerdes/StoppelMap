package com.jonasgerdes.stoppelmap.util

import android.support.annotation.LayoutRes
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder

class NoContentItem(@LayoutRes private val layout:Int) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        //no-op
    }

    override fun getLayout() = layout
}