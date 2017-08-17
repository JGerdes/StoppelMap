package com.jonasgerdes.stoppelmap.usecase.event.overview.view.event_day

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.model.events.Event
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.event_event_card.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 09.08.17
 */
class EventAdapter : RecyclerView.Adapter<EventHolder>() {

    private var eventList: List<Event> = ArrayList()
    private val openLocationSubject = BehaviorSubject.create<MapEntity>()
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
        holder.itemView.locationButton.onClick {
            eventList[holder.adapterPosition].mapEntity?.let {
                openLocationSubject.onNext(it)
            }
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    fun locationClicks(): Observable<MapEntity> {
        return openLocationSubject.hide()
    }
}