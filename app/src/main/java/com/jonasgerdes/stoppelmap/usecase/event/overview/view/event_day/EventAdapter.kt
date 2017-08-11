package com.jonasgerdes.stoppelmap.usecase.event.overview.view.event_day

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.events.Event

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.17
 */
class EventAdapter : RecyclerView.Adapter<EventHolder>() {

    private var eventList: List<Event> = ArrayList()

    var events
        get() = eventList
        set(value) {
            eventList = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent?.context).inflate(
                R.layout.event_event_card, parent, false)
        return EventHolder(view)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        holder.onBind(eventList[position])
    }

    override fun getItemCount(): Int {
        return eventList.size
    }
}