package com.jonasgerdes.stoppelmap.events.view

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.jonasgerdes.stoppelmap.events.R
import com.jonasgerdes.stoppelmap.events.entity.Day
import kotlinx.android.synthetic.main.fragment_event_day.*

class EventDayFragment : Fragment(R.layout.fragment_event_day) {

    private val day by lazy { Day(arguments!!.getInt(ARGUMENT_DAY)) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text.text = "day: " + day.id
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