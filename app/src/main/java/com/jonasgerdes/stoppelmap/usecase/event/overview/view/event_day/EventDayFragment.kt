package com.jonasgerdes.stoppelmap.usecase.event.overview.view.event_day

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.entity.map.MapEntity

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 10.08.17
 */
class EventDayFragment : Fragment() {

    //todo: refactor in presenter and interactor, there is no time right now...

    companion object {
        val ARGS_EVENT_DAY = "ARGS_EVENT_DAY"
        val ARGS_LOCATION_SLUG = "ARGS_LOCATION_SLUG"

        fun createInstance(day: Int, location: MapEntity? = null) : EventDayFragment {
            val args = Bundle()
            args.putInt(ARGS_EVENT_DAY, day)
            location?.let{
                args.putString(ARGS_LOCATION_SLUG, it.slug)
            }
            val fragment = EventDayFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(
                R.layout.event_day_fragment,
                container,
                false
        )
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}