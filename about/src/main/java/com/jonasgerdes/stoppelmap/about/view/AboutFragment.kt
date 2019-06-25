package com.jonasgerdes.stoppelmap.about.view

import android.os.Bundle
import android.util.Log
import android.view.View
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.about.R
import com.jonasgerdes.stoppelmap.core.routing.HomeDetail
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.routing.Router
import com.jonasgerdes.stoppelmap.core.util.observe
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_about.*

class AboutFragment: BaseFragment<Route.Home>(R.layout.fragment_about) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()


        toolbar.setNavigationOnClickListener {
            Router.navigateBack()
        }


    }
}