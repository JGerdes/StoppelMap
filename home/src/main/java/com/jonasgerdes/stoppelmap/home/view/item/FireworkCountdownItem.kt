package com.jonasgerdes.stoppelmap.home.view.item

import com.bumptech.glide.Glide
import com.jonasgerdes.stoppelmap.home.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_home_firework_countdown.view.*
import org.threeten.bp.Duration

data class FireworkCountdownItem(private val duration: Duration?) : Item() {
    override fun getLayout() = R.layout.item_home_firework_countdown

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            val res = context.resources

            if (duration != null) {
                val hours = duration.toHours().toInt()
                val minutes = duration.toMinutes().toInt()

                countdownText.text = if (hours == 0) {
                    res.getQuantityString(
                        R.plurals.home_card_firework_countdown_minutes, minutes, minutes
                    )
                } else {
                    res.getQuantityString(
                        R.plurals.home_card_firework_countdown_hours,
                        hours,
                        hours
                    )
                }
            } else {
                countdownText.setText(R.string.home_card_firework_greeting)
            }

            Glide.with(backgroundImage)
                .load(R.drawable.firework)
                .centerCrop()
                .into(backgroundImage)

        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return other is FireworkCountdownItem
    }


    override fun getChangePayload(newItem: com.xwray.groupie.Item<*>?): Any? {
        return duration
    }
}