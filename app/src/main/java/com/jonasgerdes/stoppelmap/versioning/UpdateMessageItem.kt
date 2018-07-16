package com.jonasgerdes.stoppelmap.versioning

import android.text.Html
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.versioning.Message
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.main_message_dialog_item.*

class UpdateMessageItem() : Item() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.setText(R.string.main_message_dialog_update_item_title)
        viewHolder.text.setText(R.string.main_message_dialog_update_item_body)
    }

    override fun getLayout() = R.layout.main_message_dialog_update_item
}