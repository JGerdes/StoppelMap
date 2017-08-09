package com.jonasgerdes.stoppelmap.usecase.transportation.route_detail.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.bumptech.glide.Glide
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.TransportationRepository
import com.jonasgerdes.stoppelmap.util.asset.Assets
import kotlinx.android.synthetic.main.transportation_route_detail_activity.*
import javax.inject.Inject


/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10-08-17
 */

class RouteDetailActivity : AppCompatActivity() {

    //todo: refactor in presenter and interactor, there is no time right now...

    @Inject lateinit var repository: TransportationRepository

    init {
        App.graph.inject(this)
    }

    companion object {
        val EXTRA_ROUTE_SLUG = "EXTRA_ROUTE_SLUG"

        fun createIntent(context: Context, routeSlug: String): Intent {
            val intent = Intent(context, RouteDetailActivity::class.java)
            intent.putExtra(EXTRA_ROUTE_SLUG, routeSlug)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transportation_route_detail_activity)
        initToolbar()
        initStationList()

    }

    private fun initToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        intent.extras?.getString(EXTRA_ROUTE_SLUG)?.let {
            repository.getRouteBy(it)?.let {
                title = it.name
            }
        }

        val headerPath = Assets.getAppHeader(Assets.HEADER_TRANSPORTATION_ROUTE)
        Glide.with(this)
                .load(Uri.parse(headerPath))
                .centerCrop()
                .into(header)
    }

    private fun initStationList() {
        intent.extras?.getString(EXTRA_ROUTE_SLUG)?.let {
            repository.getRouteBy(it)?.let {
                title = it.name
                stationList.adapter = StationAdapter()
                with(stationList.adapter as StationAdapter) {
                    stations = it.stations!!
                }
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