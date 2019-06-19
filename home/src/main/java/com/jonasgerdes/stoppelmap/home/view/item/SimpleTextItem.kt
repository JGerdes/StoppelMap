package com.jonasgerdes.stoppelmap.home.view.item

import androidx.annotation.StringRes
import com.jonasgerdes.stoppelmap.home.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_home_simple_text.view.*

data class SimpleTextItem(private @StringRes val text: Int) : Item() {
    override fun getLayout() = R.layout.item_home_simple_text

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            textContent.text = context.getString(text)
        }
    }
}