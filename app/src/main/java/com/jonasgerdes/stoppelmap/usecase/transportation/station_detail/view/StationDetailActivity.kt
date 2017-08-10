package com.jonasgerdes.stoppelmap.usecase.transportation.station_detail.view

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.SidePropagation
import android.transition.Slide
import android.view.Gravity
import android.view.MenuItem
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.TransportationRepository
import com.jonasgerdes.stoppelmap.model.entity.Station
import com.jonasgerdes.stoppelmap.util.view.SwipeBackLayout
import kotlinx.android.synthetic.main.transportation_station_detail_activity.*
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10-08-17
 */

class StationDetailActivity : AppCompatActivity() {

    //todo: refactor in presenter and interactor, there is no time right now...

    @Inject lateinit var repository: TransportationRepository

    init {
        App.graph.inject(this)
    }

    companion object {
        val EXTRA_STATION_SLUG = "EXTRA_STATION_SLUG"

        fun start(activity: Activity, station: Station) {
            val intent = Intent(activity, StationDetailActivity::class.java)
            intent.putExtra(Companion.EXTRA_STATION_SLUG, station.uuid)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val bundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
                activity.startActivity(intent, bundle)
            } else {
                activity.startActivity(intent)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transportation_station_detail_activity)
        initToolbar()
        initTransition()
        initViewPagerList()

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        intent.extras?.getString(Companion.EXTRA_STATION_SLUG)?.let {
            repository.getRouteBy(it)?.let {
                title = it.name
            }
        }
    }

    private fun initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val slide = Slide()
            slide.slideEdge = Gravity.RIGHT
            val propagation = SidePropagation()
            propagation.setPropagationSpeed(0.9f)
            slide.propagation = propagation
            window.enterTransition = slide
        }

        swipeBackLayout.setSwipeListener(object : SwipeBackLayout.SwipeListener {
            override fun onFullSwipeBack() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    finishAfterTransition()
                } else {
                    finish()
                }
            }

            override fun onSwipe(progress: Float) {
                //ignore
            }
        })
    }

    private fun initViewPagerList() {
        intent.extras?.getString(Companion.EXTRA_STATION_SLUG)?.let {
            repository.getStationBy(it)?.let {
                title = it.name
                viewpager.adapter = DepartureDayFragmentAdapter(
                        supportFragmentManager, it
                )
                tabs.setupWithViewPager(viewpager)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}