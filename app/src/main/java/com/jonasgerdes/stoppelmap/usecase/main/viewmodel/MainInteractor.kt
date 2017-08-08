package com.jonasgerdes.stoppelmap.usecase.main.viewmodel

import android.arch.lifecycle.ViewModel
import android.net.Uri
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.Settings
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.AnkoLogger

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
class MainInteractor : ViewModel(), AnkoLogger {

    private val stateSubject: BehaviorSubject<MainViewState>
            = BehaviorSubject.createDefault(MainViewState.Information())

    val state get() = stateSubject.hide()

    fun onNavigationClicked(selectedItemId: Int) {
        val newState = when (selectedItemId) {
            R.id.navigation_map -> MainViewState.Map()
            R.id.navigation_event_schedule -> MainViewState.EventSchedule()
            R.id.navigation_bus_schedule -> MainViewState.BusSchedule()
            R.id.navigation_information -> MainViewState.Information()
        //default to map
            else -> MainViewState.Map()
        }

        stateSubject.onNext(newState)
    }

    fun onIntentReceived(uri: Uri) {
        with(uri) {
            if (pathSegments.first() == Settings.URI_IDENTIFIER_VERSION
                    && pathSegments.size > 1) {

                when (pathSegments[1]) {
                    Settings.URI_IDENTIFIER_MAP -> stateSubject.onNext(MainViewState.Map(uri))
                }
            }
        }

    }
}