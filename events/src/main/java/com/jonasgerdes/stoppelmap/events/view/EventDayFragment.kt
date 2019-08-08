package com.jonasgerdes.stoppelmap.events.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.events.R
import com.jonasgerdes.stoppelmap.events.entity.Day
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_event_day.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class EventDayFragment : Fragment(R.layout.fragment_event_day) {

    private val day by lazy { Day(arguments!!.getInt(ARGUMENT_DAY)) }
    private val eventAdapter = GroupAdapter<ViewHolder>()

    private val viewModel: EventsViewModel by viewModel { parametersOf(day) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventList.adapter = eventAdapter

        observe(viewModel.events) { events ->
            animation.isVisible = false
            eventAdapter.update(events.map { event -> EventItem(event) })
        }
    }

    companion object {
        const val ARGUMENT_DAY = "argument_day"
        fun newInstance(day: Day) = EventDayFragment().apply {
            arguments = bundleOf(
                ARGUMENT_DAY to day.id
            )
        }
    }
}