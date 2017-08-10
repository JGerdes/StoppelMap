package com.jonasgerdes.stoppelmap.usecase.transportation.station_detail.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.TransportationRepository
import com.jonasgerdes.stoppelmap.model.entity.Station
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

        fun createIntent(context: Context, station: Station): Intent {
            val intent = Intent(context, StationDetailActivity::class.java)
            intent.putExtra(Companion.EXTRA_STATION_SLUG, station.uuid)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transportation_station_detail_activity)
        initToolbar()
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