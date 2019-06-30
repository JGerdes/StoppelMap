package com.jonasgerdes.stoppelmap.about.view

import com.jonasgerdes.androidutil.view.setTextOrHide
import com.jonasgerdes.stoppelmap.about.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_card_text.view.*

data class TextCard(
    val title: String? = null,
    val text: String
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.apply {
            itemView.title.setTextOrHide(title)
            itemView.text.text = text
        }
    }

    override fun getLayout() = R.layout.about_card_text

}



