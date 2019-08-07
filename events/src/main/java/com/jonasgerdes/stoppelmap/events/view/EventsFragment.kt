package com.jonasgerdes.stoppelmap.events.view

import android.os.Bundle
import android.view.View
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.events.R
import kotlinx.android.synthetic.main.fragment_events.*

class EventsFragment : BaseFragment<Route.Schedule>(R.layout.fragment_events) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()
    }
}