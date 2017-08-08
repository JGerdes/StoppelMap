package com.jonasgerdes.stoppelmap.usecase.main.viewmodel

import android.net.Uri
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.model.versioning.Message
import com.jonasgerdes.stoppelmap.model.versioning.Release

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
sealed class MainViewState(val selectedItemId: Int,
                           val message: Message?,
                           val pendingUpdate: Release?) {


    class Map(val slug: Uri? = null, message: Message? = null, pendingUpdate: Release? = null)
        : MainViewState(R.id.navigation_map, message, pendingUpdate)

    class EventSchedule(message: Message? = null, pendingUpdate: Release? = null)
        : MainViewState(R.id.navigation_event_schedule, message, pendingUpdate)

    class BusSchedule(message: Message? = null, pendingUpdate: Release? = null)
        : MainViewState(R.id.navigation_bus_schedule, message, pendingUpdate)

    class Information(message: Message? = null, pendingUpdate: Release? = null)
        : MainViewState(R.id.navigation_information, message, pendingUpdate)
}