package com.jonasgerdes.stoppelmap.usecase.event.overview.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity
import com.jonasgerdes.stoppelmap.usecase.event.overview.view.event_day.EventDayFragment
import com.jonasgerdes.stoppelmap.util.asset.StringResourceHelper
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10.08.17
 */
class EventDayFragmentAdapter(fm: FragmentManager?, val location: MapEntity? = null)
    : FragmentPagerAdapter(fm) {

    @Inject lateinit var stringRes: StringResourceHelper

    init {
        App.graph.inject(this)
    }

    override fun getItem(position: Int): Fragment {
        return EventDayFragment.createInstance(position, location)
    }

    override fun getCount(): Int {
        return 6
    }

    override fun getPageTitle(position: Int): CharSequence {
        return stringRes.getArray(R.array.days)[position]
    }
}