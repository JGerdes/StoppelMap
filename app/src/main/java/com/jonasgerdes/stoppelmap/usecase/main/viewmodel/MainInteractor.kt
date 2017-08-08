package com.jonasgerdes.stoppelmap.usecase.main.viewmodel

import android.arch.lifecycle.ViewModel
import android.net.Uri
import com.jonasgerdes.stoppelmap.App
import com.jonasgerdes.stoppelmap.R
import com.jonasgerdes.stoppelmap.Settings
import com.jonasgerdes.stoppelmap.model.versioning.VersionInfo
import com.jonasgerdes.stoppelmap.util.versioning.VersionHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import org.jetbrains.anko.AnkoLogger
import javax.inject.Inject

/**
 * @author Jonas Gerdes <dev@jonasgerdes.com>
 * @since 22.06.2017
 */
class MainInteractor : ViewModel(), AnkoLogger {

    private val stateSubject: BehaviorSubject<MainViewState>
            = BehaviorSubject.createDefault(MainViewState.Map())

    private val disposables = CompositeDisposable()

    @Inject
    protected lateinit var versionHelper: VersionHelper

    init {
        App.graph.inject(this)
        versionHelper.requestVersionInfo()
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onVersionInfoAvailable)
    }

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

    fun onVersionInfoAvailable(info: VersionInfo) {
        val currentVersion = versionHelper.versionCode
        info.latest?.let {
            if (info.latest!!.code > currentVersion) {
                stateSubject.onNext(when (stateSubject.value) {
                    is MainViewState.BusSchedule -> MainViewState.BusSchedule(null, info.latest)
                    is MainViewState.EventSchedule -> MainViewState.EventSchedule(null, info.latest)
                    is MainViewState.Information -> MainViewState.Information(null, info.latest)
                    else -> MainViewState.Map(
                            (stateSubject.value as MainViewState.Map).slug, null, info.latest
                    )
                })
            } else {
                //it's current version, time for some messages!
                val message = info.messages
                        .filter { it.versions.contains(currentVersion) }
                        .filter { it.showAlways || !versionHelper.getHasMessageBeShown(it) }
                        .firstOrNull()

                message?.let {
                    stateSubject.onNext(when (stateSubject.value) {
                        is MainViewState.BusSchedule -> MainViewState.BusSchedule(message)
                        is MainViewState.EventSchedule -> MainViewState.EventSchedule(message)
                        is MainViewState.Information -> MainViewState.Information(message)
                        else -> MainViewState.Map(
                                (stateSubject.value as MainViewState.Map).slug, message
                        )
                    })
                }
            }
        }
    }

    override fun onCleared() {
        disposables.dispose()
        super.onCleared()
    }
}