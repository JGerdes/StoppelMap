package com.jonasgerdes.stoppelmap.versioning

import android.text.Html
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.versioning.Message
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.main_message_dialog_item.*

data class HtmlMessageItem(val message: Message) : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = message.title
        viewHolder.text.text = Html.fromHtml(message.message)
    }

    override fun getLayout() = R.layout.main_message_dialog_item
}