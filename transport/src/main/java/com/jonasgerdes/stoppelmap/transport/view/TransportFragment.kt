package com.jonasgerdes.stoppelmap.transport.view

import android.os.Bundle
import android.view.View
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.transport.R
import kotlinx.android.synthetic.main.fragment_transport.*

class TransportFragment : BaseFragment<Route.Schedule>(R.layout.fragment_transport) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()

    }
}