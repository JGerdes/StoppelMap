package com.jonasgerdes.stoppelmap.bus.route

import android.annotation.SuppressLint
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.bus.station.StationActivity
import com.jonasgerdes.stoppelmap.util.observeWith
import kotlinx.android.synthetic.main.bus_route_activity.*

class RouteActivity : AppCompatActivity() {

    private val viewModel: RouteViewModel by lazy {
        ViewModelProviders.of(this).get(RouteViewModel::class.java)
    }

    val adapter = StationAdapter()

    companion object {
        private const val EXTRA_ROUTE_SLUG = "EXTRA_ROUTE_SLUG"
        private const val EXTRA_ROUTE_NAME = "EXTRA_ROUTE_NAME"
        fun start(context: Context, routeSlug: String, routeName: String?) =
                context.startActivity(Intent(context, RouteActivity::class.java).apply {
                    putExtra(EXTRA_ROUTE_SLUG, routeSlug)
                    putExtra(EXTRA_ROUTE_NAME, routeName)
                })
    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.bus_route_activity)

        stations.adapter = adapter
        stations.itemAnimator = null

        viewModel.setRoute(intent.getStringExtra(EXTRA_ROUTE_SLUG))

        val font = ResourcesCompat.getFont(this, R.font.roboto_slab_light)
        toolbarLayout.setExpandedTitleTypeface(font)
        toolbarLayout.setCollapsedTitleTypeface(font)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        intent.getStringExtra(EXTRA_ROUTE_NAME)?.let {
            toolbar.title = it
        }

        viewModel.state.observeWith(this) {
            adapter.submitList(it.stations + listOf(TransportStation.STOPPELMARKT))
        }


        adapter.events.subscribe {
            when (it) {
                is StationAdapter.StationClickedEvent.Card -> showDepartures(it.station)
                is StationAdapter.StationClickedEvent.AllDepartures -> showDepartures(it.station)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun showDepartures(station: TransportStation) {
        StationActivity.start(this, station.slug)
    }
}
