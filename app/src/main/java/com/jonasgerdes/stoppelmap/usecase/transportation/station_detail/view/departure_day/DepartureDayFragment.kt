package com.jonasgerdes.stoppelmap.usecase.transportation.station_detail.view.departure_day

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.TransportationRepository
import com.jonasgerdes.stoppelmap.model.entity.Station
import kotlinx.android.synthetic.main.transportation_station_detail_departure_day_fragment.*
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10.08.17
 */
class DepartureDayFragment : Fragment() {

    //todo: refactor in presenter and interactor, there is no time right now...

    @Inject lateinit var repository: TransportationRepository

    init {
        App.graph.inject(this)
    }

    companion object {
        val ARGS_DEPARTURE_DAY = "ARGS_DEPARTURE_DAY"
        val ARGS_STATION_SLUG = "ARGS_STATION_SLUG"

        fun createInstance(station: Station, day: Int) : DepartureDayFragment{
            val args = Bundle()
            args.putString(ARGS_STATION_SLUG, station.uuid)
            args.putInt(ARGS_DEPARTURE_DAY, day)
            val fragment = DepartureDayFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(
                R.layout.transportation_station_detail_departure_day_fragment,
                container,
                false
        )
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments.getString(ARGS_STATION_SLUG, null)?.let {
            repository.getStationBy(it)?.let {
                depatures.adapter = DepartureAdapter()
                val day = arguments.getInt(ARGS_DEPARTURE_DAY, 0)
                with(depatures.adapter as DepartureAdapter) {
                    departures = it.days!![day].departures
                }
            }
        }
    }
}