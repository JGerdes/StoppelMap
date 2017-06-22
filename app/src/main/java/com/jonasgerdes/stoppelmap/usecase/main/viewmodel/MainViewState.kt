package com.jonasgerdes.stoppelmap.usecase.main.viewmodel

import com.jonasgerdes.stoppelmap.R

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
sealed class MainViewState(val selectedItemId: Int) {


    class Map : MainViewState(R.id.navigation_map)
    class EventSchedule : MainViewState(R.id.navigation_event_schedule)
    class BusSchedule : MainViewState(R.id.navigation_bus_schedule)
    class Information : MainViewState(R.id.navigation_information)
}