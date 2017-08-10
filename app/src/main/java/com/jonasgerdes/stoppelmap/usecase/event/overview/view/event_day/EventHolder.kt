package com.jonasgerdes.stoppelmap.usecase.event.overview.view.event_day

import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.View
import com.jonasgerdes.stoppelmap.model.events.Event
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
            time.text = FORMAT_TIME.format(event.start)
            event.description?.let { description.text = Html.fromHtml(it) }

        }
    }
}