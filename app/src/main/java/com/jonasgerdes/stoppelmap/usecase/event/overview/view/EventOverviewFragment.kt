package com.jonasgerdes.stoppelmap.usecase.event.overview.view

import android.arch.lifecycle.LifecycleFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import kotlinx.android.synthetic.main.event_overview_fragment.*

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
class EventOverviewFragment : LifecycleFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.event_overview_fragment, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewPager()
    }

    private fun initViewPager() {
        viewpager.adapter = EventDayFragmentAdapter(childFragmentManager)
        tabs.setupWithViewPager(viewpager)
    }
}