package com.jonasgerdes.stoppelmap.home.view.item

import com.jonasgerdes.stoppelmap.home.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_home_countdown.view.*
import org.threeten.bp.Duration

data class CountdownItem(private val duration: Duration) : Item() {
    override fun getLayout() = R.layout.item_home_countdown

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.apply {
            val res = context.resources
            var duration = duration
            val days = duration.toDays()
            duration = duration.minusDays(days)
            val hours = duration.toHours()
            duration = duration.minusHours(hours)
            val minutes = duration.toMinutes()
            duration = duration.minusMinutes(minutes)
            val seconds = duration.seconds

            dayCount.text = days.toString()
            dayText.text = res.getQuantityText(R.plurals.home_card_countdown_day, days.toInt())

            hourCount.text = hours.toString()
            hourText.text = res.getQuantityText(R.plurals.home_card_countdown_hour, hours.toInt())

            minuteCount.text = minutes.toString()
            minuteText.text = res.getQuantityText(R.plurals.home_card_countdown_minute, minutes.toInt())

            secondCount.text = seconds.toString()
            secondText.text = res.getQuantityText(R.plurals.home_card_countdown_second, seconds.toInt())
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        return other is CountdownItem
    }


    override fun getChangePayload(newItem: com.xwray.groupie.Item<*>?): Any? {
        return duration
    }
}