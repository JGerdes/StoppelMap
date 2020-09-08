package com.jonasgerdes.stoppelmap.home.view.item

import androidx.annotation.StringRes
import com.jonasgerdes.stoppelmap.core.util.fromHtml
import com.jonasgerdes.stoppelmap.home.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_home_title_and_description_card.view.*

data class TitleAndDescriptionCardItem(
    @StringRes private val titleRes: Int,
    @StringRes private val descriptionRes: Int
) : Item() {
    override fun getLayout() = R.layout.item_home_title_and_description_card

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            title.text = context.getString(titleRes)
            description.text = context.getString(descriptionRes).fromHtml()
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TitleAndDescriptionCardItem) {
            return false
        }

        return titleRes == other.titleRes && descriptionRes == other.descriptionRes
    }
}