package com.jonasgerdes.stoppelmap.usecase.transportation.station_detail.view

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.Station
import com.jonasgerdes.stoppelmap.usecase.transportation.station_detail.view.departure_day.DepartureDayFragment
import com.jonasgerdes.stoppelmap.util.asset.StringResourceHelper
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10.08.17
 */
class DepartureDayFragmentAdapter(fm: FragmentManager?, val station: Station)
    : FragmentPagerAdapter(fm) {

    @Inject lateinit var stringRes: StringResourceHelper

    init {
        App.graph.inject(this)
    }

    override fun getItem(position: Int): Fragment {
        return DepartureDayFragment.createInstance(station, position)
    }

    override fun getCount(): Int {
        return 6
    }

    override fun getPageTitle(position: Int): CharSequence {
        return stringRes.getArray(R.array.days)[position]
    }
}