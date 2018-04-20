package com.jonasgerdes.stoppelmap

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jonasgerdes.stoppelmap.about.AboutFragment
import com.jonasgerdes.stoppelmap.event.list.BusListFragment
import com.jonasgerdes.stoppelmap.event.list.EventListFragment
import com.jonasgerdes.stoppelmap.event.list.FeedFragment
import com.jonasgerdes.stoppelmap.map.MapFragment
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    private val fragments = mapOf(
            R.id.navigation_map to MapFragment(),
            R.id.navigation_event_schedule to EventListFragment(),
            R.id.navigation_bus_schedule to BusListFragment(),
            R.id.main_navigation_social_feed to FeedFragment(),
            R.id.main_navigation_about to AboutFragment()
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        navigation.setOnNavigationItemSelectedListener {
            showFragment(it.itemId)
            true
        }
        showFragment(R.id.navigation_map)
    }

    private fun showFragment(itemId: Int) {
        fragments[itemId]?.let {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.content, it)
                    .disallowAddToBackStack()
                    .commitNow()
        }
    }


}
