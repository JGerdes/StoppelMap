package com.jonasgerdes.stoppelmap.about

import com.jonasgerdes.stoppelmap.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.about_card_author.*

data class AuthorCard(
        val name: String
) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.name.text = name
    }

    override fun getLayout() = R.layout.about_card_author

}

