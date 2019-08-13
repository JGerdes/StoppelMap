package com.jonasgerdes.stoppelmap.events.view

import android.os.Bundle
import android.view.View
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.jonasgerdes.androidutil.view.consumeWindowInsetsTop
import com.jonasgerdes.stoppelmap.core.routing.Route
import com.jonasgerdes.stoppelmap.core.widget.BaseFragment
import com.jonasgerdes.stoppelmap.events.R
import com.jonasgerdes.stoppelmap.events.entity.Day
import kotlinx.android.synthetic.main.fragment_events.*

class EventsFragment : BaseFragment<Route.Schedule>(R.layout.fragment_events) {

    private val fragmentAdapter by lazy {
        object : FragmentStateAdapter(this) {
            override fun getItemCount() = 6

            override fun createFragment(position: Int) = EventDayFragment.newInstance(Day(position))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.consumeWindowInsetsTop()

        dayPager.adapter = fragmentAdapter
        TabLayoutMediator(tabs, dayPager) { tab, position ->
            tab.text = resources.getStringArray(R.array.generic_day_abbreviation)[position]
        }.attach()
    }
}