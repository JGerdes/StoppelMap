package com.jonasgerdes.stoppelmap.about

import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.util.setTextOrHide
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_card_text.*

data class TextCard(
        val title: String? = null,
        val text: String
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.setTextOrHide(title)
        viewHolder.text.text = text
    }

    override fun getLayout() = R.layout.about_card_text

}



