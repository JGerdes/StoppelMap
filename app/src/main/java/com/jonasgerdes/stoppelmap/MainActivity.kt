package com.jonasgerdes.stoppelmap

import android.os.Bundle
import android.support.v4.view.ViewCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.jonasgerdes.stoppelmap.about.AboutFragment
import com.jonasgerdes.stoppelmap.event.list.BusListFragment
import com.jonasgerdes.stoppelmap.event.list.EventListFragment
import com.jonasgerdes.stoppelmap.event.list.FeedFragment
import com.jonasgerdes.stoppelmap.map.MapFragment
import com.jonasgerdes.stoppelmap.util.enableItemShifting
import com.jonasgerdes.stoppelmap.util.enableItemTextHiding
import com.jonasgerdes.stoppelmap.util.toggleLayoutFullscreen
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : AppCompatActivity() {

    private val fragments = mapOf(
            R.id.navigation_map to MapFragment(),
            R.id.navigation_event_schedule to EventListFragment(),
            R.id.navigation_bus_schedule to BusListFragment(),
            R.id.main_navigation_social_feed to FeedFragment(),
            R.id.main_navigation_about to AboutFragment()
    )

    private var lastId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        initNavigation()
        toggleLayoutFullscreen(true)
        showFragment(lastId, R.id.navigation_map)

    }

    private fun initNavigation() {
        navigation.enableItemShifting(false)
        navigation.enableItemTextHiding(true)
        navigation.setOnNavigationItemSelectedListener {
            showFragment(lastId, it.itemId)
            lastId = it.itemId
            true
        }

    }

    private fun showFragment(from: Int, to: Int) {
        val transaction = supportFragmentManager.beginTransaction()

        if (from != -1) {
            transaction.hide(fragments[from])
        }
        var fragment = supportFragmentManager.findFragmentByTag(to.toString()+ resources.configuration.orientation)
        if (fragment == null) {
            fragment = fragments[to]
            transaction.add(R.id.content, fragment, to.toString()+ resources.configuration.orientation)
        } else {
            transaction.show(fragments[to])
        }
        transaction.commit()
        ViewCompat.requestApplyInsets(content)
    }

}
