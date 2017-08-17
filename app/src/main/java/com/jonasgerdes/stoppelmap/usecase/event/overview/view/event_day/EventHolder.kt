package com.jonasgerdes.stoppelmap.usecase.event.overview.view.event_day

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import android.widget.TextView
import com.jonasgerdes.stoppelmap.model.events.Event
import kotlinx.android.synthetic.main.event_event_card.view.*
import kotlinx.android.synthetic.main.event_event_content.view.*
import java.text.SimpleDateFormat

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10.08.17
 */
class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val FORMAT_TIME = SimpleDateFormat("kk:mm")

    fun onBind(event: Event) {
        with(itemView) {
            name.text = event.name
            time.text = "${FORMAT_TIME.format(event.start)} Uhr"

            setOrHide(description, Html.fromHtml(event.description ?: ""))
            setOrHide(location, event.mapEntity?.name ?: "")
            setOrHide(people, event.artists?.map { it.value }?.joinToString(", ") ?: "")

            if(event.mapEntity != null) {
                locationButton.visibility = View.VISIBLE
            } else {
                locationButton.visibility = View.GONE
            }
        }
    }

    private fun setOrHide(view: TextView, text: CharSequence?) {
        if (text != null && !text.trim().isEmpty()) {
            view.visibility = View.VISIBLE
            view.text = text
        } else {
            view.visibility = View.GONE
        }
    }
}