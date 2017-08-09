package com.jonasgerdes.stoppelmap.usecase.transportation.station_detail.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jonasgerdes.stoppelmap.R

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10-08-17
 */

class StationDetailActivity : AppCompatActivity() {

    //todo: refactor in presenter and interactor, there is no time right now...

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.transportation_station_detail_activity)
    }
}